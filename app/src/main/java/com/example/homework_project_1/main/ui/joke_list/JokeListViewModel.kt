package com.example.homework_project_1.main.ui.joke_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped

class JokeListViewModel: ViewModel() {
    private val _jokes = MutableLiveData<List<ViewTyped>>()
    val jokes: MutableLiveData<List<ViewTyped>> = _jokes

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> = _error

    fun generateJokes() {
        _jokes.value = JokesGenerator.generateJokesData()
    }

    fun showGeneratedData() {
        _jokes.value = JokesGenerator.getSelectedJokes()

    }
}