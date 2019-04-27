package com.alifyz.newsapp.api

import com.alifyz.newsapp.data.entity.News
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    fun fetchNews(@Query("q") query : String = "brasil",
                  @Query("language") language : String = "pt",
                  @Query("page") pageToken : String)
            : Deferred<Response<News>>
}