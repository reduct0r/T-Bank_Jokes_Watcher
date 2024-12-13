package com.example.homework_project_1.main.data.repository

import android.util.Log
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.withLock

object JokesRepositoryImpl : Repository {

    private val jokeDb: JokesWatcherDatabase = JokesWatcherDatabase.getInstance(App.instance)
    private val shownJokes = mutableListOf<Int>()
    private val categories = mutableSetOf<String>()

    override suspend fun deleteJoke(id: Int) {
        jokeDb.jokeDao().delete(id)
    }

    override suspend fun insertJoke(joke: JokeDTO) {
        jokeDb.jokeDao().insert(joke.toDbEntity())
    }

    override suspend fun fetchRandomJokes(amount: Int): List<JokeDTO> {
        val jokes = jokeDb.jokeDao().getRandomJokes(amount)
        jokes.forEach{shownJokes.add(it.id!!)}

        jokeDb.jokeDao().markShown(true, shownJokes) // Обновляем статус в базе
        return jokes.map { it.toDto() }
    }

    override suspend fun updateJoke(joke: JokeDTO) {
        jokeDb.jokeDao().update(joke.toDbEntity())
    }

    override suspend fun resetUsedJokes() {
        jokeDb.jokeDao().markUnShown()
        shownJokes.clear()
    }

    override suspend fun getAmountOfJokes(): Int {
        return jokeDb.jokeDao().getAmountOfJokes()
    }

    fun getUserJokesAfter(lastTimestamp: Long): Flow<List<JokeDbEntity>> {
        return jokeDb.jokeDao().getUserJokesAfter(lastTimestamp)
    }

    suspend fun getCategories(): List<String> {
        jokeDb.jokeDao().getCategories().map {
            categories.add(it)
        }
        return categories.toList()
    }

    fun addNewCategory(newCategory: String) {
        categories.add(newCategory)
    }

    suspend fun getAllUserJokes(): List<JokeDTO> {
        return jokeDb.jokeDao().getAllUserJokes().map { it.toDto() }
    }


}