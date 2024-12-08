package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.domain.repository.Repository

class DeleteJokeUseCase(private val repository: Repository) {
    suspend operator fun invoke(jokeId: Int) {
        repository.deleteJoke(jokeId)
    }
}