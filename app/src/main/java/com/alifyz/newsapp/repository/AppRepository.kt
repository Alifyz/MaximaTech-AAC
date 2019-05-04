package com.alifyz.newsapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alifyz.newsapp.api.ApiFactory
import com.alifyz.newsapp.api.NewsApi
import com.alifyz.newsapp.data.AppDatabase
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.data.entity.Data
import com.alifyz.newsapp.data.entity.News
import com.alifyz.newsapp.data.entity.Source
import com.alifyz.newsapp.paging.PaginationUtils
import com.alifyz.newsapp.paging.PagingCallback
import kotlinx.coroutines.*

/**
 * Nosso repositório é a principal camada responsável por comunicar
 * com o Banco de Dados local e com nossas API remotas.
 * Dessa forma, para manipular e expor métodos para as camadas superiores
 * devemos ter acesso ao objeto Database do Room e a nossa API WebService
 */
class AppRepository(context : Context) {

    //Instância do Banco Room
    private val database : AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    //Instância do Retrofit
    private val webService : NewsApi by lazy {
        ApiFactory.newsApi
    }

    //Busca por uma Notícia específica no Banco por ID
    fun searchNews(id : Long) = database.DAO().searchNews(id)

    //Retorna a DataSource do Banco Room para construir a lista paginada.
    fun getPaginatedHeadlines() : LiveData<PagedList<Article>> {

        val dataSource = database.DAO().loadAllPaginadedNews()

        val pagingCallback = PagingCallback(this)

        val data = LivePagedListBuilder(dataSource, PaginationUtils.pageConfig)
            .setBoundaryCallback(pagingCallback)
            .build()

        return data
    }

    //Método responsável por conectar a API e inserir mais notícias no banco.
    fun fetchHeadlinesFromNetwork(pageId : Int = 1) {
        CoroutineScope(Dispatchers.IO).launch {
            val endpoint = webService.fetchNews(pageToken = pageId.toString())
            withContext(Dispatchers.Main){
                val request = endpoint.await()
                if(request.isSuccessful) {
                    storeHeadlinesFromNetwork(request.body())
                } else {
                    Log.e("Status: ${request.code()}:", request.message())
                }
            }
        }
    }

    //Método usado para Armazenar Notícias e Fontes em forma de Transaction
    fun addNewsAndSources(article : Article, source : Source) {
        CoroutineScope(Dispatchers.IO).launch {
            database.DAO().insertNewsAndSource(article, source)
        }
    }

    //Usado pelo PagingCallback para retornar a última página visualizada.
    fun getPaginationData() : Data?  = runBlocking{
        CoroutineScope(Dispatchers.IO).async {
            database.DAO().loadPagingData()
        }.await()
    }

    //Usado pelo PagingCallback para salvar a última página visualizada.
    fun savePaginationData(data : Data) {
        CoroutineScope(Dispatchers.IO).launch {
            database.DAO().savePageToken(data)
        }
    }

    /**
     * Usado pelo Método abaixo para Armazenar Itens no Banco após serem baixados da API
     * @see fetchHeadlinesFromNetwork
     */
    private fun storeHeadlinesFromNetwork(news : News?){
        val articles = news?.articles
        articles?.map {article ->
           addNewsAndSources(article, article.source ?:
           Source(newsId = article.hashCode(), sourceId = "Desconhecido", name = "Desconhecido"))
        }
    }
}