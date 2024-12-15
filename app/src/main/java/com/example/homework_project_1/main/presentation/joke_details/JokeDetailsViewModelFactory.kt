package com.example.homework_project_1.main.presentation.joke_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework_project_1.main.presentation.utils.ViewTyped

// Фабрика для создания ViewModel
@Suppress("UNCHECKED_CAST")
class JokeDetailsViewModelFactory(
    private val assistedFactory: JokeDetailsViewModel.Factory,
    private val gotJoke: ViewTyped.JokeUIModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JokeDetailsViewModel::class.java)) {
            return assistedFactory.create(gotJoke) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}