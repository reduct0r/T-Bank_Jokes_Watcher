package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeEntity
import com.example.homework_project_1.main.data.model.toDto
import kotlinx.coroutines.delay

object JokeRepositoryImpl : Repository {
    private val apiService = ApiServiceImpl.getInstance()

    override suspend fun fetchJokes(amount: Int): List<JokeDTO> {
        delay(500)
        return try {
            apiService.getJokes(amount).jokes.map { jokeEntity ->
                jokeEntity.toDto(flags = jokeEntity.flags)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}