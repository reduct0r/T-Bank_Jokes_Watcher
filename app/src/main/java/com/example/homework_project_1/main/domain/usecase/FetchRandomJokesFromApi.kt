package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.repository.ApiRepositoryImpl

class FetchRandomJokesFromApi {
    suspend operator fun invoke(amount: Int): List<JokeDTO> {
        val jokes = ApiRepositoryImpl.fetchRandomJokes(amount)
        return jokes
    }
}
