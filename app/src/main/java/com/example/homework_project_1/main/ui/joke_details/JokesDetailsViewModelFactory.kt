package com.example.homework_project_1.main.ui.joke_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Фабрика для создания ViewModel
@Suppress("UNCHECKED_CAST")
class JokesDetailsViewModelFactory(private val jokePosition: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(JokeDetailsViewModel::class.java)) {
            JokeDetailsViewModel(jokePosition) as T // TODO: UNCHECKED_CAST  idk how to fix it
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}