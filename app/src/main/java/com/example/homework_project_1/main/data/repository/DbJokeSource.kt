package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase

class DbJokeSource(private val jokeDb: JokesWatcherDatabase) {

    // Database
    suspend fun exists(id: Int): Boolean {
        // Вернуть true, если запись с данным id уже есть
        val cursor = jokeDb.query("SELECT 1 FROM jokes WHERE id = ?", arrayOf(id.toString()))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    suspend fun dropJokesTable() {
        jokeDb.jokeDao().dropJokesTable()
    }

    suspend fun setDbJoke(joke: JokeDbEntity) {
        jokeDb.jokeDao().insert(joke)
    }

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