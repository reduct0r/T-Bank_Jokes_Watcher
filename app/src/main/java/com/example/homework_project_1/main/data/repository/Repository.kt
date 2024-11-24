package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeEntity

interface Repository {
    suspend fun fetchJokes(amount: Int): List<JokeDTO>
}