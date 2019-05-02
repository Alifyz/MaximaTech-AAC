package com.alifyz.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alifyz.newsapp.R

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
