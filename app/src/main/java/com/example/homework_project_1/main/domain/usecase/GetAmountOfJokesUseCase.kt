package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.domain.repository.Repository

class GetAmountOfJokesUseCase(private val repository: Repository) {
    suspend operator fun invoke(): Int {
        return repository.getAmountOfJokes()
    }
}