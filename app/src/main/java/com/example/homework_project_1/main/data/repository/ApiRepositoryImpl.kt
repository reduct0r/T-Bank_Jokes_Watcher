package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.model.JokeApiEntity
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex

import kotlinx.coroutines.sync.withLock
import javax.inject.Inject


class ApiRepositoryImpl @Inject constructor(
    private var apiServiceImpl: ApiServiceImpl
) : Repository {

    private val mutex = Mutex()

    override suspend fun deleteJoke(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun insertJoke(joke: JokeDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRandomJokes(amount: Int, needMark: Boolean): List<JokeDTO> = mutex.withLock {
        return apiServiceImpl.getJokes(amount).jokes.map { jokeEntity: JokeApiEntity ->
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

    override fun getUserJokesAfter(lastTimestamp: Long): Flow<List<JokeDbEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun isJokeExists(joke: JokeDTO): Boolean {
        TODO("Not yet implemented")
    }
}