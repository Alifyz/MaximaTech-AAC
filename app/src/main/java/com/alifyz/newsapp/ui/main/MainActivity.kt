package com.alifyz.newsapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.alifyz.newsapp.R
import com.alifyz.newsapp.adapters.NewsAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val adapter = NewsAdapter()

        viewModel.getPaginadedNews().observe(this, Observer {
            adapter.submitList(it)
        })

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)*/
    }
}
