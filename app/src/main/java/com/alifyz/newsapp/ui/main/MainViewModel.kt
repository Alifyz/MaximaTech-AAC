package com.alifyz.newsapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.repository.AppRepository

class MainViewModel(
    applicationContext: Application
) : AndroidViewModel(applicationContext) {

    private val repository by lazy {
        AppRepository(applicationContext)
    }

    fun getNews(): LiveData<List<Article>> {
        return repository.getCachedHeadlines()
    }
}