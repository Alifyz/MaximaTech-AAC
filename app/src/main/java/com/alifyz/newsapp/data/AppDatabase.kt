package com.alifyz.newsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alifyz.newsapp.data.dao.NewsDao
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.data.entity.Source

@Database(entities = [Article::class, Source::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun DAO() : NewsDao

    companion object {

    }
}