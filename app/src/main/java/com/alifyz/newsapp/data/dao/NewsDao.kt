package com.alifyz.newsapp.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.data.entity.Data
import com.alifyz.newsapp.data.entity.Source

@Dao
interface NewsDao {

    @Query("SELECT * FROM articles")
    fun loadAllNews() : LiveData<List<Article>>

    @Query("SELECT * FROM articles")
    fun loadAllPaginadedNews() : DataSource.Factory<Int, Article>

    @Query("SELECT * from data")
    fun loadPagingData() : Data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewSource(source : Source)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewArticle(article : Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePageToken(data : Data)

    @Transaction
    fun insertNewsAndSource(article : Article, source : Source) {
        addNewArticle(article)
        addNewSource(source)
    }
}