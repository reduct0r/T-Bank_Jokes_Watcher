package com.example.homework_project_1.main.domain.repository

import com.example.homework_project_1.main.data.model.JokeDTO

interface Repository {
    suspend fun deleteJoke(id: Int)
    suspend fun insertJoke(joke: JokeDTO)
    suspend fun fetchRandomJokes(amount: Int): List<JokeDTO>
    suspend fun updateJoke(joke: JokeDTO)
    suspend fun resetUsedJokes()
    suspend fun getAmountOfJokes(): Int

}