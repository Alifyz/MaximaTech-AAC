package com.alifyz.newsapp.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class RefreshNews(context: Context, params : WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            //Operações que exigem muito da CPU
            //OU tarefas async.
            //1 - Realizar um Upload
            //2 - Processar alguma Imagem etc
            delay(10000)
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}