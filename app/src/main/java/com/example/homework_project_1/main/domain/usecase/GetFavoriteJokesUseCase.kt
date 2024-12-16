package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.domain.repository.JokesRepository
import javax.inject.Inject

class GetFavoriteJokesUseCase @Inject constructor(
    private val repository: JokesRepository
) {
    suspend operator fun invoke(): List<JokeDTO> {
        return repository.getFavoriteJokes()
    }
}