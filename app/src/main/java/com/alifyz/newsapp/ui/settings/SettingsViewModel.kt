package com.alifyz.newsapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.alifyz.newsapp.background.ImageProcessingWorker

class SettingsViewModel : ViewModel() {

    private val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(true)
        .build()

    private val worker = OneTimeWorkRequestBuilder<ImageProcessingWorker>()
        .addTag("ImageProcessing")
        .setConstraints(constraints)
        .build()


    private val workStatus  : LiveData<WorkInfo>

    init {
        workStatus = WorkManager.getInstance().getWorkInfoByIdLiveData(worker.id)
    }

    fun startWorker() {
        WorkManager.getInstance().enqueue(worker)
    }

    fun workerStatus() = workStatus
}