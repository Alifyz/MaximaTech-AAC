package com.alifyz.newsapp.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alifyz.newsapp.repository.AppRepository
import kotlinx.coroutines.delay

class RefreshNews(context: Context, params : WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repository = AppRepository(applicationContext)

        try {
            delay(10000)
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}