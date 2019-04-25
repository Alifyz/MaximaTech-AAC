package com.alifyz.newsapp.api

import com.alifyz.newsapp.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    //Interceptor que adiciona API_KEY em todas as Requisições
    private val authInterceptor = Interceptor {chain ->

        //Captura a URL atual para adicionar um novo parâmetro
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("apiKey", BuildConfig.API_KEY)
            .build()

        //Construíndo um novo Request baseado no antigo e com URL nova
        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        //Passando nossa nova Request para continuar a requisição
        chain.proceed(newRequest)
    }

    //Cliente HTTP
    private val client = OkHttpClient()
        .newBuilder()
        .addInterceptor(authInterceptor)
        .build()

    private fun getRetrofitInstance() : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val newsApi : NewsApi
        get() = getRetrofitInstance().create(NewsApi::class.java)

}