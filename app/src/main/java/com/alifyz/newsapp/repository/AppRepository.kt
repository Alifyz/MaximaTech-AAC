package com.alifyz.newsapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.alifyz.newsapp.data.AppDatabase
import com.alifyz.newsapp.data.entity.Article

class AppRepository(context : Context) {

    val database : AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    fun getHeadlines() : LiveData<List<Article>> {
        return database.DAO().loadAllNews()
    }
}