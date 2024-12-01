package com.example.homework_project_1.main.data.repository

import android.util.Log
import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import kotlinx.coroutines.flow.Flow

class DbJokeSource(private val jokeDb: JokesWatcherDatabase) {

    private val shownJokes = mutableListOf<Int>()
    private val shownCachedJokes = mutableSetOf<Int>()

    fun getDbUserJokes() : Flow<List<JokeDbEntity>> {
        return jokeDb.jokeDao().getUserJokes()
    }

    suspend fun getCategories(): List<String> {
        return jokeDb.jokeDao().getCategories()
    }

    suspend fun resetJokesSequence() {
        jokeDb.jokeDao().resetJokesSequence()
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
        val jokes = jokeDb.jokeDao().getRandomJokes(amount)
        jokes.forEach{shownJokes.add(it.id!!)}

        jokeDb.jokeDao().markShown(true, shownJokes) // Обновляем статус в базе
        return jokes
    }

    suspend fun resetUsedJokes() {
        jokeDb.jokeDao().markUnShown()
        shownJokes.clear()
    }

    suspend fun getAllDbJokes(): List<JokeDbEntity> {
        return jokeDb.jokeDao().getAllJokes()
    }

    // Database Cache
    suspend fun getAllCachedJokes(): List<JokeDbEntity> {
        return jokeDb.jokeDao().getAllJokes()
    }

    suspend fun getCachedJokeById(id: Int): JokeDbEntity {
        return jokeDb.jokeDao().getJokeById(id)
    }

    suspend fun getRandomCachedJokes(amount: Int): List<JokeDbEntity> {
        val allJokes = jokeDb.jokeDao().getRandomJokes(amount * 2)
        val result = allJokes.filterNot { shownCachedJokes.contains(it.id) }
            .take(amount)
        shownCachedJokes.addAll(result.map { it.id!! })
        return result
    }

    fun resetUsedCachedJokes() {
        shownCachedJokes.clear()
    }
}
