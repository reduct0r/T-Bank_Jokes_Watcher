package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toCacheEntity
import com.example.homework_project_1.main.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
    private val jokeDb: JokesWatcherDatabase
) : Repository {

    private val shownCachedJokes = mutableListOf<Int>()

    override suspend fun deleteJoke(id: Int) {
        jokeDb.jokeDao().deleteCache(id)
    }

    override suspend fun insertJoke(joke: JokeDTO) {
        jokeDb.jokeDao().insertCache(joke.toCacheEntity())
    }

    override suspend fun fetchRandomJokes(amount: Int, needMark: Boolean): List<JokeDTO> {
        val jokes = jokeDb.jokeDao().getRandomCacheJokes(amount)

        if (needMark) {
            jokes.forEach { shownCachedJokes.add(it.id!!) }
            jokeDb.jokeDao().markCacheShown(true, shownCachedJokes) // Обновляем статус в базе
        }
        return jokes.map { it.toDto().apply { source = JokeSource.CACHE } }
    }

    override suspend fun updateJoke(joke: JokeDTO) {
        jokeDb.jokeDao().updateCache(joke.toCacheEntity())
    }

    override suspend fun resetUsedJokes() {
        jokeDb.jokeDao().markCacheUnShown()
        shownCachedJokes.clear()
    }

    override suspend fun getAmountOfJokes(): Int {
        TODO("Not yet implemented")
    }

    override fun getUserJokesAfter(lastTimestamp: Long): Flow<List<JokeDbEntity>> {
        TODO("Not yet implemented")
    }

}