package com.example.homework_project_1.main.ui.joke_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Фабрика для создания ViewModel
@Suppress("UNCHECKED_CAST")
class JokesViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JokeListViewModel::class.java) -> {
                JokeListViewModel() as T   // TODO: UNCHECKED_CAST  idk how to fix it
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}