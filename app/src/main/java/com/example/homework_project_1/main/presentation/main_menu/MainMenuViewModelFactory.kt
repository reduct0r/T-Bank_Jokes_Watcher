package com.example.homework_project_1.main.presentation.main_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.homework_project_1.main.di.annotations.CacheRepository
import com.example.homework_project_1.main.di.annotations.JokesRepository
import com.example.homework_project_1.main.domain.generator.JokesGenerator
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import com.example.homework_project_1.main.domain.usecase.GetAmountOfJokesUseCase
import com.example.homework_project_1.main.domain.usecase.InsertJokeUseCase
import javax.inject.Inject
import javax.inject.Provider


@Suppress("UNCHECKED_CAST")
class MainMenuViewModelFactory @Inject constructor(
    @JokesRepository private val fetchRandomJokesFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @CacheRepository private val fetchRandomCacheFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @JokesRepository private val getAmountOfJokesUseCase: GetAmountOfJokesUseCase,
    @JokesRepository private val insertJokeUseCase: InsertJokeUseCase,
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