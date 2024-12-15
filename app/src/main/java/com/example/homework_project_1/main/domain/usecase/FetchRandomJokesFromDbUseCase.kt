package com.example.homework_project_1.main.domain.usecase

import android.util.Log
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.domain.repository.Repository
import javax.inject.Inject

class FetchRandomJokesFromDbUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(amount: Int): List<JokeDTO> {
        Log.d("FetchFromDb", "${repository::class.java}")
        return repository.fetchRandomJokes(amount)
    }
}