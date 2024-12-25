package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.repository.JokesRepository
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import javax.inject.Inject

class AddToFavouritesUseCase @Inject constructor(
    @JokesRepositoryA private var repository: JokesRepository
) {
    suspend operator fun invoke(joke: ViewTyped.JokeUIModel) {
        if (repository.countIfJokeExists(joke.toDto()) > 0) {
                repository.changeFavouriteStatus(joke.id, joke.isFavorite)
        } else {
            repository.insertJoke(joke.toDto())
        }
    }
}