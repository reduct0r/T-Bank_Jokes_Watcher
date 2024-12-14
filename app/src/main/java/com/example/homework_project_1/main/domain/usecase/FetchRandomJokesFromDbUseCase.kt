package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.domain.repository.Repository

class FetchRandomJokesFromDbUseCase(private val repository: Repository) {
    suspend operator fun invoke(amount: Int): List<JokeDTO> {
        return repository.fetchRandomJokes(amount)
    }
}