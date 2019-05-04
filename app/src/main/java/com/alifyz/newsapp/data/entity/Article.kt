package com.alifyz.newsapp.data.entity

data class Article (
    var id : Long = 0,
    var author: String? = "",
    var content: String? = "",
    var description: String? = "",
    var publishedAt: String? = "",
    var title: String? = "",
    var url: String? = "",
    var urlToImage: String? = "",
    var source: Source? = null
)