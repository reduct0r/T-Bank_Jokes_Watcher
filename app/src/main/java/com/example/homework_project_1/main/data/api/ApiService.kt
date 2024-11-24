package com.example.homework_project_1.main.data.api

import com.example.homework_project_1.main.data.model.JokeResponse

interface ApiService {
    suspend fun getJokes(amount: Int = 10): JokeResponse
}