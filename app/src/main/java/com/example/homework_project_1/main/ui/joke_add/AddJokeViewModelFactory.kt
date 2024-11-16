package com.example.homework_project_1.main.ui.joke_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework_project_1.main.data.JokesRepository

@Suppress("UNCHECKED_CAST")
class AddJokeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddJokeViewModel::class.java)) {
            AddJokeViewModel() as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}