package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.domain.repository.Repository
import javax.inject.Inject

class GetAmountOfJokesUseCase@Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Int {
        return repository.getAmountOfJokes()
    }
}