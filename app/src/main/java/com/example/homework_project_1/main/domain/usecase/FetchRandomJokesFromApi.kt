package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.repository.ApiRepositoryImpl
import com.example.homework_project_1.main.domain.repository.Repository
import javax.inject.Inject

class FetchRandomJokesFromApi @Inject constructor(
    private var apiRepositoryImpl: Repository
) {
    suspend operator fun invoke(amount: Int): List<JokeDTO> {
        val jokes = apiRepositoryImpl.fetchRandomJokes(amount)
        jokes.forEach { it.source = JokeSource.NETWORK }
        return jokes
    }
}
