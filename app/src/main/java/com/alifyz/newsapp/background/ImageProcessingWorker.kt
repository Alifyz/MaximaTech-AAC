package com.alifyz.newsapp.background

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.alifyz.newsapp.R
import java.lang.Exception

class ImageProcessingWorker(context: Context, params: WorkerParameters)