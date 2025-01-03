package com.example.homework_project_1.main.presentation.joke_add

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AddJokeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddJokeViewModel::class.java)) {
            return AddJokeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
