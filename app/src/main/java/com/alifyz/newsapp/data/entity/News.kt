package com.alifyz.newsapp.data.entity

data class News (
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)