package com.example.homework_project_1.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [JokeDbEntity::class, JokeCacheEntity::class], version = 11)
@TypeConverters(Converters::class)
abstract class JokesWatcherDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDAO

}