package com.alifyz.newsapp.background

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
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

            Log.d("Image Worker: ", "Job done!")
            Result.success()

        } catch (e: Exception) {
            Log.d("Image Worker: ", "Job failed!")
            Result.failure()
        }
    }
}