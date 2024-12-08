package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeApiEntity
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toCacheEntity
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toDbEntity
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object RepositoryImpl : Repository {
    private val mutex = Mutex()

    private val apiJokeSource = ApiJokeSource(ApiServiceImpl.getInstance())
    private val dbJokeSource = DbJokeSource(JokesWatcherDatabase.getInstance(App.instance))

    private val categories = mutableSetOf<String>()

    // API
    suspend fun fetchApiJokes(amount: Int): List<JokeDTO> = mutex.withLock {
        return apiJokeSource.getJokes(amount).map { jokeEntity: JokeApiEntity ->
            jokeEntity.toDto(flags = jokeEntity.flags).apply {
                source = JokeSource.NETWORK
            }
        }
    }

    // Database


    suspend fun setMark(mark: Boolean, shown: List<Int>) {
        dbJokeSource.setMark(mark, shown)
    }

    suspend fun update(joke: JokeDTO) {
        dbJokeSource.update(joke)
    }

    suspend fun getJokesAmount(): Int{
        return dbJokeSource.getJokesAmount()
    }

    suspend fun dropJokesTable() {
        dbJokeSource.dropJokesTable()
    }
    suspend fun resetJokesSequence() {
        dbJokeSource.resetJokesSequence()
    }

    suspend fun insertDbJoke(joke: JokeDTO) = mutex.withLock {
        dbJokeSource.setDbJoke(joke.toDbEntity())

        if (joke.category !in categories ) {
            categories.add(joke.category)
        }
    }



    suspend fun fetchRandomDbJokes(amount: Int): List<JokeDTO> = mutex.withLock {
        delay(500)
        return dbJokeSource.getRandomDbJokes(amount).map { jokeEntity ->
            jokeEntity.toDto()
        }
    }

    suspend fun getCategories(): List<String> = mutex.withLock {
        dbJokeSource.getCategories().map {
            categories.add(it)
        }
        return categories.toList()
    }

    fun addNewCategory(newCategory: String) {
        categories.add(newCategory)
    }




    // Database Cache
    suspend fun deleteDeprecatedCache(lastTimestamp: Long): Boolean {
        return dbJokeSource.deleteDeprecatedCache(lastTimestamp)
    }

    suspend fun insertCacheJoke(joke: JokeDTO) = mutex.withLock {
        dbJokeSource.setCacheJoke(joke.toCacheEntity())
        categories.add(joke.category)
    }

    suspend fun getCacheAmount(): Int {
        return dbJokeSource.getCacheAmount()
    }

     suspend fun fetchCacheJoke(id: Int): JokeDTO {
        return dbJokeSource.getCachedJokeById(id).toDto()
    }

    suspend fun markCacheShown(){
        dbJokeSource.markCacheShown()
    }

     suspend fun fetchAllCacheJokes(amount: Int): List<JokeDTO> {
        return dbJokeSource.getAllCachedJokes().map { jokeEntity ->
            jokeEntity.toDto().apply {
                source = JokeSource.CACHE
            }
        }
    }


    suspend fun fetchRandomCacheJokes(amount: Int): List<JokeDTO> = mutex.withLock {
        return dbJokeSource.getRandomCachedJokes(amount).map { jokeEntity ->
            jokeEntity.toDto().apply {
                source = JokeSource.CACHE
            }
        }
    }

    suspend fun resetCachedJokes() = mutex.withLock {
        dbJokeSource.resetUsedCachedJokes()
    }

    override suspend fun deleteJoke(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun insertJoke(joke: JokeDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRandomJokes(amount: Int): List<JokeDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun updateJoke(joke: JokeDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun resetUsedJokes() {
        TODO("Not yet implemented")
    }

    override suspend fun getAmountOfJokes(): Int {
        TODO("Not yet implemented")
    }

}