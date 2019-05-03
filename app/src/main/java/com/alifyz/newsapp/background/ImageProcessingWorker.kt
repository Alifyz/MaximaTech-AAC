package com.alifyz.newsapp.background

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.alifyz.newsapp.R
import java.lang.Exception

class ImageProcessingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        makeStatusNotification("Bluring Image", applicationContext)

        return try {

            val picture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.galaxy
            )

            val blurImage = blurBitmap(picture, applicationContext)

            val fileUri = writeBitmapToFile(applicationContext, blurImage)

            makeStatusNotification("BlurImage URI: $fileUri", applicationContext)
            Result.success()

        } catch (e: Exception) {

            Result.failure()
        }
    }
}