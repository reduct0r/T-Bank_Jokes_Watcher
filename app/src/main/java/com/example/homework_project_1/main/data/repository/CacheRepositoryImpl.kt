package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toCacheEntity

object CacheRepositoryImpl: Repository {

    private val jokeDb: JokesWatcherDatabase = JokesWatcherDatabase.getInstance(App.instance)
    private val shownCachedJokes = mutableListOf<Int>()

    override suspend fun deleteJoke(id: Int) {
        jokeDb.jokeDao().deleteCache(id)
    }

    override suspend fun insertJoke(joke: JokeDTO) {
        jokeDb.jokeDao().insertCache(joke.toCacheEntity())
    }

    override suspend fun fetchRandomJokes(amount: Int): List<JokeDTO> {
        val jokes = jokeDb.jokeDao().getRandomCacheJokes(amount)
        jokes.forEach{ shownCachedJokes.add(it.id!!)}

        jokeDb.jokeDao().markCacheShown(true, shownCachedJokes) // Обновляем статус в базе
        return jokes.map{ it.toDto().apply { source = JokeSource.CACHE } }
    }

    override suspend fun updateJoke(joke: JokeDTO) {
        jokeDb.jokeDao().updateCache(joke.toCacheEntity())
    }

    override suspend fun resetUsedJokes() {
        jokeDb.jokeDao().markCacheUnShown()
        shownCachedJokes.clear()
    }

    override suspend fun getAmountOfJokes(): Int {
        TODO("Not yet implemented")
    }

    suspend fun deleteDeprecatedCache(lastTimestamp: Long): Boolean {
        val deprecatedCache = jokeDb.jokeDao().getCachedJokesBefore(lastTimestamp)
        if (deprecatedCache.isEmpty()){
            return false
        } else {
            deprecatedCache.forEach { jokeDb.jokeDao().delete(it.id!!) }
            return true
        }
    }
}