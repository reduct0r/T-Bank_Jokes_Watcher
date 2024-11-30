package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.api.ApiService

class ApiJokeSource(private val apiService: ApiService) {
    suspend fun getJokes(amount: Int): List<Unit> {
        return apiService.getJokes(amount).jokes.map { jokeEntity ->
            jokeEntity.toDto(jokeEntity.flags)
        }
    }
}