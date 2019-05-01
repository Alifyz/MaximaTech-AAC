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

class AppRepository(context : Context) {

    private val database : AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    private val webService : NewsApi by lazy {
        ApiFactory.newsApi
    }

    fun getCachedHeadlines() : LiveData<List<Article>> {
        return database.DAO().loadAllNews()
    }

    fun getPaginatedHeadlines() : LiveData<PagedList<Article>> {

        val dataSource = database.DAO().loadAllPaginadedNews()

        val pagingCallback = PagingCallback(this)

        val data = LivePagedListBuilder(dataSource, PaginationUtils.pageConfig)
            .setBoundaryCallback(pagingCallback)
            .build()

        return data
    }

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

    private fun storeHeadlinesFromNetwork(news : News?){
        val articles = news?.articles
        articles?.map {article ->
           addNewsAndSources(article, article.source ?:
           Source(newsId = article.hashCode(), sourceId = "Desconhecido", name = "Desconhecido"))
        }
    }

    fun addNewsAndSources(article : Article, source : Source) {
        CoroutineScope(Dispatchers.IO).launch {
            database.DAO().insertNewsAndSource(article, source)
        }
    }

    fun getPaginationData() : Data?  = runBlocking{
        getAsyncData().await()
    }

    private fun getAsyncData() = CoroutineScope(Dispatchers.IO).async {
        database.DAO().loadPagingData()
    }

    fun savePaginationData(data : Data) {
        CoroutineScope(Dispatchers.IO).launch {
            database.DAO().savePageToken(data)
        }
    }
}