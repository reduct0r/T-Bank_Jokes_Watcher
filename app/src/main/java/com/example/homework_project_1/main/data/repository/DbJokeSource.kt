package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.database.JokeDAO

class DbJokeSource(private val jokeDao: JokeDAO) {
    suspend fun getCachedJokes(): List<Unit> {
        return jokeDao.getAllJokes().map { jokeEntity ->
            jokeEntity.toDto(jokeEntity.flags)
        }
    }
}