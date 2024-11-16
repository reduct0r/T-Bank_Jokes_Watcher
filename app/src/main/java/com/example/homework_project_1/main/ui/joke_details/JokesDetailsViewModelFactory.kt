package com.example.homework_project_1.main.ui.joke_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Фабрика для создания ViewModel
@Suppress("UNCHECKED_CAST")
class JokesDetailsViewModelFactory(private val jokePosition: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JokeDetailsViewModel::class.java) -> {
                JokeDetailsViewModel(jokePosition) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

