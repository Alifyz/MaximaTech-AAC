package com.alifyz.newsapp.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.data.entity.Data

@Dao
interface NewsDao {

    @Insert
    fun insert(articleList : List<Article>?)

    @Query("SELECT * FROM articles")
    fun loadAllNews() : LiveData<List<Article>>

    @Query("SELECT * FROM articles")
    fun loadAllPaginadedNews() : DataSource.Factory<Int, Article>

    @Query("SELECT * from data where id = 1")
    fun loadPagingData() : Data

}