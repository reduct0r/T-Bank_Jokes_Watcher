package com.example.homework_project_1.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [JokeEntity::class], version = 1)
abstract class JokeWatcherDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDAO
}