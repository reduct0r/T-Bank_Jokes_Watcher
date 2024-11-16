package com.example.homework_project_1.main.ui.joke_details

import androidx.lifecycle.*
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped.JokeUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokeDetailsViewModel(private val jokePosition: Int) : ViewModel() {
    private val _jokeUIModel = MutableLiveData<JokeUIModel>()
    val jokeUIModel: LiveData<JokeUIModel> get() = _jokeUIModel

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Загрузка шутки с использованием корутин
    fun loadJoke() {
        viewModelScope.launch {
            try {
                val selectedJokes = withContext(Dispatchers.IO) {
                    JokesGenerator.getSelectedJokes() // Долгий вызов
                }

                if (jokePosition in selectedJokes.indices) {
                    val item = selectedJokes[jokePosition]
                    if (item is JokeUIModel) {
                        _jokeUIModel.value = item
                    } else {
                        _error.value = "Incorrect joke type."
                    }
                } else {
                    _error.value = "Incorrect position."
                }
            } catch (e: Exception) {
                _error.value = "Error loading joke: ${e.message}"
            }
        }
    }

    fun getPosition(): Int {
        return jokePosition
    }
}