package com.example.homework_project_1.main.domain.usecase

import android.util.Log
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import javax.inject.Inject

class AddToFavouritesUseCase @Inject constructor(
    private var jokeDb: JokesWatcherDatabase
) {
    suspend operator fun invoke(joke: ViewTyped.JokeUIModel) {
        val jokeEnt = joke.toDbEntity()
        if (jokeDb.jokeDao().isJokeExists(
                jokeEnt.id!!,
                jokeEnt.category,
                jokeEnt.question,
                jokeEnt.answer)
            ){
            jokeDb.jokeDao().updateFavouriteStatus(jokeEnt.id, jokeEnt.isFavourite)
        } else {
            jokeDb.jokeDao().insert(jokeEnt)
        }
    }
}