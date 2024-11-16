package com.example.homework_project_1.main.ui.joke_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.data.Joke
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.convertToUiModel
import kotlinx.coroutines.launch

class JokeDetailsViewModel(private val jokePosition: Int) : ViewModel() {
    private val _joke = MutableLiveData<ViewTyped.JokeUIModel>()
    val joke: LiveData<ViewTyped.JokeUIModel> get() = _joke

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Загрузка шутки
    fun loadJoke() {
        viewModelScope.launch {
            try {
                val selectedJokes = JokesGenerator.getSelectedJokes()
                val uiModel = convertToUiModel(selectedJokes, false)
                if (jokePosition in uiModel.indices) {
                    _joke.value = uiModel[jokePosition]
                } else {
                    _error.value = "Incorrect position."
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun getPosition(): Int {
        return jokePosition
    }
}