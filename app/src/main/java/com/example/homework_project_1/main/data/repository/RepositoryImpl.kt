package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.toDto

object JokeRepositoryImpl : Repository {
    private val apiService = ApiServiceImpl.getInstance()

    override suspend fun fetchJokes(amount: Int): List<JokeDTO> {
        return apiService.getJokes(amount).jokes.map { jokeEntity ->
            jokeEntity.toDto(flags = jokeEntity.flags).apply {
                source = JokeSource.NETWORK
            }
        }
    }
}