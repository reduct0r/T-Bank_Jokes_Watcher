package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.domain.repository.Repository
import javax.inject.Inject

class DeleteJokeUseCase @Inject constructor(
    private var repository: Repository
) {
    suspend operator fun invoke(jokeId: Int) {
        repository.deleteJoke(jokeId)
    }
}