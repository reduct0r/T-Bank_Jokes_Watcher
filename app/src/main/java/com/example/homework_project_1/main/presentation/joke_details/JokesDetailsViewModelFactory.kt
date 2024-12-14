package com.example.homework_project_1.main.presentation.joke_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework_project_1.main.presentation.utils.ViewTyped

// Фабрика для создания ViewModel
@Suppress("UNCHECKED_CAST")
class JokesDetailsViewModelFactory(private val joke: ViewTyped.JokeUIModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(JokeDetailsViewModel::class.java)) {
            JokeDetailsViewModel(joke) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
