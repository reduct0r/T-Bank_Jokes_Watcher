package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.model.JokeDTO

interface Repository {

    // API
    suspend fun fetchApiJokes(amount: Int): List<JokeDTO>

    // Database
    suspend fun existsDbJoke(id: Int): Boolean
    suspend fun dropJokesTable()

    suspend fun insertDbJoke(joke: JokeDTO)

    suspend fun fetchDbJoke(id: Int): JokeDTO
    suspend fun fetchCacheJoke(id: Int): JokeDTO

    suspend fun fetchAllDbJokes(amount: Int): List<JokeDTO>
    suspend fun fetchAllCacheJokes(amount: Int): List<JokeDTO>

    suspend fun fetchRandomCacheJokes(amount: Int): List<JokeDTO>
    suspend fun fetchRandomDbJokes(amount: Int): List<JokeDTO>
}