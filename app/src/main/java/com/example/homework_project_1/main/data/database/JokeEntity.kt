package com.example.homework_project_1.main.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "jokes")
data class JokeEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val question: String,
    val answer: String,
    val source: String,
    val avatarUri: String?
)
