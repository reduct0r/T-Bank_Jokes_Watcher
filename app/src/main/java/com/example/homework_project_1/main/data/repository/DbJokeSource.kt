package com.example.homework_project_1.main.data.repository

import android.util.Log
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.database.JokeCacheEntity
import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toDbEntity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map

class DbJokeSource(private val jokeDb: JokesWatcherDatabase) {

    private val shownJokes = mutableListOf<Int>()
    private val shownCachedJokes = mutableSetOf<Int>()

    fun getUserJokesAfter(lastTimestamp: Long): Flow<List<JokeDbEntity>> {
        return jokeDb.jokeDao().getUserJokesAfter(lastTimestamp)
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
        if (jokeDb.jokeDao().checkIfExists(joke.question, joke.answer, joke.category) == 0) {
            jokeDb.jokeDao().insert(joke)
        }
        else{
            throw Exception("Joke already exists")
        }
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

    suspend fun update(jokeDTO: JokeDTO){
        jokeDb.jokeDao().update(jokeDTO.toDbEntity(App.instance))
    }

    suspend fun setMark(mark: Boolean, shown: List<Int>) {
        jokeDb.jokeDao().markShown(true, shown)
    }

    suspend fun resetUsedJokes() {
        jokeDb.jokeDao().markUnShown()
        shownJokes.clear()
    }

    suspend fun getAllDbJokes(): List<JokeDbEntity> {
        return jokeDb.jokeDao().getAllJokes()
    }

    suspend fun getJokesAmount(): Int{
        return jokeDb.jokeDao().getAllJokes().size
    }

    // Database Cache
    suspend fun setCacheJoke(joke: JokeCacheEntity) {
        if (jokeDb.jokeDao().checkIfCacheExists(joke.question, joke.answer, joke.category) == 0) {
            jokeDb.jokeDao().insertCache(joke)
        }
    }

    suspend fun getAllCachedJokes(): List<JokeCacheEntity> {
        return jokeDb.jokeDao().getAllCacheJokes()
    }

    suspend fun getCachedJokeById(id: Int): JokeDbEntity {
        return jokeDb.jokeDao().getJokeById(id)
    }

    suspend fun getRandomCachedJokes(amount: Int): List<JokeCacheEntity> {
        val allJokes = jokeDb.jokeDao().getRandomCacheJokes(amount)
        val result = allJokes.filterNot { shownCachedJokes.contains(it.id) }
            .take(amount)
        shownCachedJokes.addAll(result.map { it.id!! })
        return result
    }

    suspend fun resetUsedCachedJokes() {
        jokeDb.jokeDao().markCacheUnShown()
        shownCachedJokes.clear()
    }

    suspend fun markCacheShown(){
        jokeDb.jokeDao().markCacheShown()
    }

    suspend fun getCacheAmount(): Int{
        return jokeDb.jokeDao().getAllCacheJokes().size
    }
}
