package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.model.JokeDTO

interface Repository {
    suspend fun fetchJokes(amount: Int): List<JokeDTO>
}