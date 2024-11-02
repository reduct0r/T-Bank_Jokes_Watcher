package com.example.homework_project_1.main.ui.joke_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped.Joke

class JokeDetailsViewModel(private val jokePosition: Int) : ViewModel() {
    private val _joke = MutableLiveData<Joke>()
    val joke: LiveData<Joke> get() = _joke

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        loadJoke()
    }

    private fun loadJoke() {
        val selectedJokes = JokesGenerator.getSelectedJokes()
        if (jokePosition in selectedJokes.indices) {
            val item = selectedJokes[jokePosition]
            if (item is Joke) {
                _joke.value = item
            } else {
                _error.value = "Incorrect joke type."
            }
        } else {
            _error.value = "Incorrect position."
        }
    }
}