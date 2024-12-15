package com.example.homework_project_1.main.domain.repository

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.model.JokeDTO
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun deleteJoke(id: Int)
    suspend fun insertJoke(joke: JokeDTO)
    suspend fun fetchRandomJokes(amount: Int, needMark: Boolean = true): List<JokeDTO>
    suspend fun updateJoke(joke: JokeDTO)
    suspend fun resetUsedJokes()
    suspend fun getAmountOfJokes(): Int
    fun getUserJokesAfter(lastTimestamp: Long): Flow<List<JokeDbEntity>>

}