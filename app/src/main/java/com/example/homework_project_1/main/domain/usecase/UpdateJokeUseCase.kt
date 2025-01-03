package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.domain.repository.Repository

class UpdateJokeUseCase(private val repository: Repository) {
    suspend operator fun invoke(joke: JokeDTO) {
        repository.updateJoke(joke)
    }
}