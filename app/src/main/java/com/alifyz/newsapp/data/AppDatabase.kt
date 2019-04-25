package com.alifyz.newsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alifyz.newsapp.data.dao.NewsDao
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.data.entity.Source

@Database(entities = [Article::class, Source::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun DAO() : NewsDao

    companion object {

        @Volatile
        private var instance : AppDatabase? = null
        fun getInstance(context : Context) : AppDatabase {
            return instance ?: synchronized(this){
                instance ?: buildInstance(context)
            }
        }

        private fun buildInstance(context: Context) : AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
                .addCallback(object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)


                    }
                }).build()
        }
    }
}