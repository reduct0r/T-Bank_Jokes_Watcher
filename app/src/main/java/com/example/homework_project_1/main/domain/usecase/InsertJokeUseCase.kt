package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.domain.repository.Repository
import javax.inject.Inject

class InsertJokeUseCase@Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(joke: JokeDTO) {
        repository.insertJoke(joke)
    }
}