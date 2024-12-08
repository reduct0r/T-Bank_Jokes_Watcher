package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.model.JokeApiEntity
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.domain.repository.Repository
import kotlinx.coroutines.sync.Mutex

import kotlinx.coroutines.sync.withLock

object ApiRepositoryImpl: Repository {
    private val mutex = Mutex()

    override suspend fun deleteJoke(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun insertJoke(joke: JokeDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRandomJokes(amount: Int): List<JokeDTO> = mutex.withLock {
        return ApiServiceImpl.getInstance().getJokes(amount).jokes.map { jokeEntity: JokeApiEntity ->
            jokeEntity.toDto(flags = jokeEntity.flags).apply { source = JokeSource.NETWORK }
        }
    }

    override suspend fun updateJoke(joke: JokeDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun resetUsedJokes() {
        TODO("Not yet implemented")
    }

    override suspend fun getAmountOfJokes(): Int {
        TODO("Not yet implemented")
    }
}