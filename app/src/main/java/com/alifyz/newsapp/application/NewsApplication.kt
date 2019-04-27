package com.alifyz.newsapp.application

import android.app.Application
import com.facebook.stetho.Stetho

class NewsApplication  : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}