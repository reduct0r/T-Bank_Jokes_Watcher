package com.example.homework_project_1.main.ui.joke_list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.JokesRepository

class JokeListViewModel(context: Context): ViewModel() {
    private val _jokes = MutableLiveData<List<ViewTyped>>()
    val jokes: MutableLiveData<List<ViewTyped>> = _jokes

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> = _error

    suspend fun generateJokes() {
        _jokes.value = JokesGenerator.generateJokesData()
    }

    fun showGeneratedData(): List<ViewTyped> {
        return JokesGenerator.getSelectedJokes()
    }

    fun resetJokes() {
        JokesGenerator.reset()
    }
}
