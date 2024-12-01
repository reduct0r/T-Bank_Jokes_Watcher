package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeApiEntity
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toDbEntity
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object RepositoryImpl : Repository {

    private val apiJokeSource = ApiJokeSource(ApiServiceImpl.getInstance())
    private val dbJokeSource = DbJokeSource(JokesWatcherDatabase.getInstance(App.instance))

    private val categories = mutableSetOf<String>()

    // API
    override suspend fun fetchApiJokes(amount: Int): List<JokeDTO> {
        return apiJokeSource.getJokes(amount).map { jokeEntity: JokeApiEntity ->
            jokeEntity.toDto(flags = jokeEntity.flags).apply {
                source = JokeSource.NETWORK
            }
        }
    }

    // Database
    fun getDbUserJokes(): Flow<List<JokeDTO>> {
        return dbJokeSource.getDbUserJokes().map { jokeDbEntityList ->
            jokeDbEntityList.map { jokeDbEntity ->
                jokeDbEntity.toDto().also { it.source = JokeSource.USER }
            }
        }
    }

    override suspend fun dropJokesTable() {
        dbJokeSource.dropJokesTable()
    }
    override suspend fun resetJokesSequence() {
        dbJokeSource.resetJokesSequence()
    }

    override suspend fun insertDbJoke(joke: JokeDTO) {
        dbJokeSource.setDbJoke(joke.toDbEntity(App.instance))

        if (joke.category !in categories ) {
            categories.add(joke.category)
        }
    }

    override suspend fun fetchDbJoke(id: Int): JokeDTO {
        return dbJokeSource.getDbJokeById(id).toDto().apply {
                source = JokeSource.DATABASE
        }
    }

    override suspend fun fetchAllDbJokes(amount: Int): List<JokeDTO> {
        return dbJokeSource.getAllDbJokes().map { jokeEntity ->
            jokeEntity.toDto().apply {
                source = JokeSource.DATABASE
            }
        }
    }

    override suspend fun fetchRandomDbJokes(amount: Int): List<JokeDTO> {
        delay(500)
        return dbJokeSource.getRandomDbJokes(amount).map { jokeEntity ->
            jokeEntity.toDto()
        }
    }

    suspend fun getCategories(): List<String> {
        return dbJokeSource.getCategories()
    }

    fun addNewCategory(newCategory: String) {
        categories.add(newCategory)
    }

    suspend fun resetUsedJokes(){
        dbJokeSource.resetUsedJokes()
    }


    // Database Cache
    override suspend fun fetchCacheJoke(id: Int): JokeDTO {
        return dbJokeSource.getCachedJokeById(id).toDto().apply {
            source = JokeSource.DATABASE
        }
    }

    override suspend fun fetchAllCacheJokes(amount: Int): List<JokeDTO> {
        return dbJokeSource.getAllCachedJokes().map { jokeEntity ->
            jokeEntity.toDto().apply {
                source = JokeSource.CACHE
            }
        }
    }


    override suspend fun fetchRandomCacheJokes(amount: Int): List<JokeDTO> {
        return dbJokeSource.getRandomCachedJokes(amount).map { jokeEntity ->
            jokeEntity.toDto().apply {
                source = JokeSource.CACHE
            }
        }
    }

}