package com.alifyz.newsapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alifyz.newsapp.data.entity.Article

@Dao
interface NewsDao {

    @Insert
    fun insert(articleList : List<Article>?)

    @Query("SELECT * FROM articles")
    fun loadAllNews() : LiveData<List<Article>>

}