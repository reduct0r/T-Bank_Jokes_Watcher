package com.example.homework_project_1.main.data.repository

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.toDbEntity
import com.example.homework_project_1.main.domain.repository.JokesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JokesRepositoryImpl @Inject constructor(
    private val jokeDb: JokesWatcherDatabase
) : JokesRepository {
    private val shownJokes = mutableListOf<Int>()
    private val categories = mutableSetOf<String>()

    override suspend fun deleteJoke(id: Int) {
        jokeDb.jokeDao().delete(id)
    }

    override suspend fun insertJoke(joke: JokeDTO) {
        jokeDb.jokeDao().insert(joke.toDbEntity())
    }

    override suspend fun fetchRandomJokes(amount: Int, needMark: Boolean): List<JokeDTO> {
        val jokes = jokeDb.jokeDao().getRandomJokes(amount)

        if (needMark) {
            jokes.forEach { shownJokes.add(it.id!!) }
            jokeDb.jokeDao().markShown(true, shownJokes) // Обновляем статус в базе
        }
        return jokes.map { it.toDto() }
    }

    override suspend fun updateJoke(joke: JokeDTO) {
        jokeDb.jokeDao().update(joke.toDbEntity())
    }

    override suspend fun resetUsedJokes() {
        jokeDb.jokeDao().markUnShown()
        shownJokes.clear()
    }

    override suspend fun getAmountOfJokes(): Int {
        return jokeDb.jokeDao().getAmountOfJokes()
    }

    override fun getUserJokesAfter(lastTimestamp: Long): Flow<List<JokeDbEntity>> {
        return jokeDb.jokeDao().getUserJokesAfter(lastTimestamp)
    }

    override suspend fun getCategories(): List<String> {
        jokeDb.jokeDao().getCategories().map {
            categories.add(it)
        }
        return categories.toList()
    }

    override fun addNewCategory(newCategory: String) {
        categories.add(newCategory)
    }

    override suspend fun getFavoriteJokes(): List<JokeDTO> {
        return jokeDb.jokeDao().getFavouriteJokes().map { it.toDto() }
    }

    suspend fun getAllUserJokes(): List<JokeDTO> {
        return jokeDb.jokeDao().getAllUserJokes().map { it.toDto() }
    }

    override suspend fun changeFavouriteStatus(jokeId: Int, isFavourite: Boolean) {
        jokeDb.jokeDao().updateFavouriteStatus(jokeId, isFavourite)
    }

    override suspend fun countIfJokeExists(joke: JokeDTO): Int {
        return jokeDb.jokeDao().checkIfExists(joke.id, joke.question, joke.answer, joke.category)
    }

    override suspend fun isJokeDataExists(joke: JokeDTO): Boolean {
        return jokeDb.jokeDao().isJokeDataExists(
            joke.category,
            joke.question,
            joke.answer
        )
    }


}