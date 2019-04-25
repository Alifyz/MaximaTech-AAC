package com.alifyz.newsapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alifyz.newsapp.R
import com.alifyz.newsapp.api.ApiFactory
import com.alifyz.newsapp.utils.Result
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
