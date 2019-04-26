package com.alifyz.newsapp.utils

import android.content.Context
import android.util.JsonReader
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alifyz.newsapp.data.AppDatabase
import com.alifyz.newsapp.data.entity.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override val coroutineContext = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        try{
            applicationContext.assets.open("news.json").use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->

                    val newsType = object : TypeToken<List<Article>>(){}.type

                    val planList : List<Article> = Gson().fromJson(jsonReader.toString(), newsType)

                    val database = AppDatabase.getInstance(applicationContext)

                    database.DAO().insert(planList)

                    Result.success()
                }
            }
        }catch (e: Exception) {
            Result.failure()
        }
    }
}