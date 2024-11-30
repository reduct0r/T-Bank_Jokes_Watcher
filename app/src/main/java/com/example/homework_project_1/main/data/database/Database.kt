package com.example.homework_project_1.main.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JokeDbEntity::class], version = 1)
abstract class JokesWatcherDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDAO

    companion object {
        @Volatile
        lateinit var INSTANCE: JokesWatcherDatabase

        fun initDatabase(context: Context) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                JokesWatcherDatabase::class.java,
                "jokes_watcher_database"
            ).build()
            INSTANCE = instance
        }
    }
}