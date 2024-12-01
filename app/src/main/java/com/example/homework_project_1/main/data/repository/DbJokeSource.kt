package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase

class DbJokeSource(private val jokeDb: JokesWatcherDatabase) {

    // Database
    suspend fun getDbJokeById(id: Int): JokeDbEntity {
        return jokeDb.jokeDao().getJokeById(id)
    }

    suspend fun getRandomDbJokes(amount: Int): List<JokeDbEntity> {
        return jokeDb.jokeDao().getRandomJokes(amount)
    }

    suspend fun getAllDbJokes(): List<JokeDbEntity> {
        return jokeDb.jokeDao().getAllJokes()
    }

    // Database Cache
    suspend fun getAllCachedJokes(): List<JokeDbEntity> {
        return jokeDb.jokeDao().getAllJokes()
    }

    suspend fun getCachedJokeById(id: Int): List<JokeDbEntity> {
        return jokeDb.jokeDao().getAllJokes()
    }

    suspend fun getRandomCachedJokes(amount: Int): List<JokeDbEntity> {
        return jokeDb.jokeDao().getRandomJokes(amount)
    }


}