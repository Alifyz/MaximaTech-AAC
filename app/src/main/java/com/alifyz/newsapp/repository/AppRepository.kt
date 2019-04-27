package com.alifyz.newsapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alifyz.newsapp.adapters.NewsAdapter
import com.alifyz.newsapp.api.ApiFactory
import com.alifyz.newsapp.api.NewsApi
import com.alifyz.newsapp.data.AppDatabase
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.data.entity.Data
import com.alifyz.newsapp.paging.PaginationUtils
import com.alifyz.newsapp.paging.PagingCallback

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

    fun fetchHeadlinesFromNetwork(pageToken : Int = 1) {

    }

    fun getPaginationData() : Data {
        return database.DAO().loadPagingData()
    }

    fun updateCachedNews() {

    }
}