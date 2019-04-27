package com.alifyz.newsapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(
    @PrimaryKey(autoGenerate = false)
    var id : Long = 1,
    var lastPage : Int)