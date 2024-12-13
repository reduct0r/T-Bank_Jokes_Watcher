package com.example.homework_project_1.main.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [JokeDbEntity::class, JokeCacheEntity::class], version = 6)
@TypeConverters(Converters::class)
abstract class JokesWatcherDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDAO

    companion object {
        @Volatile
        var INSTANCE: JokesWatcherDatabase? = null

        fun getInstance(context: Context): JokesWatcherDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    JokesWatcherDatabase::class.java,
                    "jokes_watcher_database"
                )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}