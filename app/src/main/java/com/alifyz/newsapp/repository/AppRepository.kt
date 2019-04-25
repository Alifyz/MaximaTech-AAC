package com.alifyz.newsapp.repository

import android.util.Log
import com.alifyz.newsapp.api.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppRepository {

    fun getHeadlines() {
        CoroutineScope(Dispatchers.IO).launch {
            val service = ApiFactory.newsApi.getHeadlines()
            withContext(Dispatchers.Main) {
                val request = service.await()
                if(request.isSuccessful) {
                    Log.d("Request: ", request.toString())
                } else{
                    Log.d("Request: Error ", request.toString())
                }
            }
        }
    }
}