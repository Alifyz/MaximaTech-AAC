package com.alifyz.newsapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Long = 0,
    var author: String? = "",
    var content: String? = "",
    var description: String? = "",
    var publishedAt: String? = "",
    var title: String? = "",
    var url: String? = "",
    var urlToImage: String? = "",
    @Ignore
    var source: Source? = null
)