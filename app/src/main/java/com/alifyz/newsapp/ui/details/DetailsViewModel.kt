package com.alifyz.newsapp.ui.details


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alifyz.newsapp.repository.AppRepository

class DetailsViewModel(applicationContext: Application)  : AndroidViewModel(applicationContext) {

    private val repository by lazy {
        AppRepository(applicationContext)
    }

    fun searchNews(id : Long) = repository.searchNews(id)

}