package com.example.homework_project_1.main.data.repository

import android.util.Log
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.Flags
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeApiEntity

object RepositoryImpl : Repository {

    private val apiJokeSource = ApiJokeSource(ApiServiceImpl.getInstance())
    private val dbJokeSource = DbJokeSource(JokesWatcherDatabase.getInstance(App.instance))


    // API
    override suspend fun fetchApiJokes(amount: Int): List<JokeDTO> {
        return apiJokeSource.getJokes(amount).map { jokeEntity: JokeApiEntity ->
            jokeEntity.toDto(flags = jokeEntity.flags).apply {
                source = JokeSource.NETWORK
            }
        }
    }

    // Database
    override suspend fun fetchDbJoke(id: Int): JokeDTO {
        return dbJokeSource.getDbJokeById(id).toDto().apply {
                source = JokeSource.DATABASE
            }
//        catch (e: Exception) {
//            JokeDTO(1,
//                null,
//                null,
//                JokeSource.DATABASE.toString(),
//                "Error",
//                "Error",
//                JokeSource.DATABASE,
//                Flags(false, false, false, false, false, false),
//                "Error",
//            )
//        }
    }

    override suspend fun fetchAllDbJokes(amount: Int): List<JokeDTO> {
        return dbJokeSource.getAllDbJokes().map { jokeEntity ->
            jokeEntity.toDto().apply {
                source = JokeSource.DATABASE
            }
        }
    }


    override suspend fun fetchRandomDbJokes(amount: Int): List<JokeDTO> {
        return dbJokeSource.getRandomDbJokes(amount).map { jokeEntity ->
            jokeEntity.toDto().apply {
                source = JokeSource.DATABASE
            }
        }
    }

    // Database Cache
    override suspend fun fetchCacheJoke(id: Int): JokeDTO {
        return dbJokeSource.getCachedJokeById(id).first().toDto().apply {
            source = JokeSource.CACHE
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

    fun getStr(): String {
        return "str"
    }

}