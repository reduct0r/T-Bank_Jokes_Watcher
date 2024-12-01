package com.example.homework_project_1.main.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.homework_project_1.main.data.api.ApiClient
import com.example.homework_project_1.main.data.api.ApiServiceImpl


@Database(entities = [JokeDbEntity::class], version = 3)
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