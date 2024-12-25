package com.example.homework_project_1.main.presentation.main_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework_project_1.main.di.annotations.CacheRepositoryA
import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.generator.JokesGenerator
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import com.example.homework_project_1.main.domain.usecase.GetAmountOfJokesUseCase
import com.example.homework_project_1.main.domain.usecase.InsertJokeUseCase
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class MainMenuViewModelFactory @Inject constructor(
    @JokesRepositoryA private val fetchRandomJokesFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @CacheRepositoryA private val fetchRandomCacheFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @JokesRepositoryA private val getAmountOfJokesUseCase: GetAmountOfJokesUseCase,
    @JokesRepositoryA private val insertJokeUseCase: InsertJokeUseCase,
    private val jokesGenerator: JokesGenerator
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainMenuViewModel::class.java)) {
            return MainMenuViewModel(
                fetchRandomJokesFromDbUseCase,
                fetchRandomCacheFromDbUseCase,
                getAmountOfJokesUseCase,
                insertJokeUseCase,
                jokesGenerator
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}