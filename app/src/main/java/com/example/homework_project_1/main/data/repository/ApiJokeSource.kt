package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.api.ApiService
import com.example.homework_project_1.main.data.model.JokeApiEntity

class ApiJokeSource(private val apiService: ApiService) {
    suspend fun getJokes(amount: Int): List<JokeApiEntity> {
        return apiService.getJokes(amount).jokes
    }
}