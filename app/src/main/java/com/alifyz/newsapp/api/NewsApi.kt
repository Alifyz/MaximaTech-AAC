package com.alifyz.newsapp.api

import com.alifyz.newsapp.data.entity.News
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    fun getHeadlines(@Query("country") country : String)
            : Deferred<Response<News>>

    @GET("everything")
    fun fetchNews(@Query("q") query : String, @Query("language") language : String = "pt")
            : Deferred<Response<News>>
}