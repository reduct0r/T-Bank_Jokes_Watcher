package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import kotlinx.coroutines.flow.Flow

class DbJokeSource(private val jokeDb: JokesWatcherDatabase) {

    private val shownJokes = mutableSetOf<Int>()

    fun getDbUserJokes() : Flow<List<JokeDbEntity>> {
        return jokeDb.jokeDao().getUserJokes()
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
        val allJokes = jokeDb.jokeDao().getRandomJokes(amount * 2)
        val result = allJokes.filterNot { shownJokes.contains(it.id) }
            .take(amount) // Оставим только те, которые ещё не были показаны
        // Сохраним id показанных
        shownJokes.addAll(result.map { it.id!! })
        return result
    }

    fun resetUsedJokes() {
        shownJokes.clear()
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