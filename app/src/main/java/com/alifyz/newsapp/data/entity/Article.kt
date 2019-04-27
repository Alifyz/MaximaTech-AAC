package com.alifyz.newsapp.data.entity

import androidx.room.*
import java.util.*
import javax.annotation.Nullable

@Entity(tableName = "articles", indices = arrayOf(Index(value = ["content"], unique = true)))
data class Article (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,
    @Nullable
    var author: String? = "",
    @ColumnInfo(name = "content")
    @Nullable
    var content: String? = "",
    @Nullable
    var description: String? = "",
    @Nullable
    var publishedAt: String? = "",
    @Nullable
    var title: String? = "",
    @Nullable
    var url: String? = "",
    @Nullable
    var urlToImage: String? = "",
    @Ignore
    var source: Source? = null
)