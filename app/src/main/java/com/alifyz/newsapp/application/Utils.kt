package com.alifyz.newsapp.application

import android.widget.ImageView
import com.alifyz.newsapp.R
import com.bumptech.glide.Glide

fun ImageView.loadRemote(url : String) {
    Glide.with(this.context)
        .load(url)
        .error(R.drawable.nopic_homebox)
        .into(this)
}

@JvmName("loadRemoteNullable")
fun ImageView.loadRemote(url : String?) {
    Glide.with(this.context)
        .load(url)
        .error(R.drawable.nopic_homebox)
        .into(this)
}