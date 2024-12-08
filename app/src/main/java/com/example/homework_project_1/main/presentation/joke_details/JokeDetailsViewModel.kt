package com.example.homework_project_1.main.presentation.joke_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import kotlinx.coroutines.launch

class JokeDetailsViewModel(private val gotJoke: ViewTyped.JokeUIModel) : ViewModel() {
    private val _joke = MutableLiveData<ViewTyped.JokeUIModel>()
    val joke: LiveData<ViewTyped.JokeUIModel> get() = _joke

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        loadJoke()
    }

    fun loadJoke() {
        _joke.value = gotJoke
    }

    fun addToFavorites() {
        viewModelScope.launch {
            try {
                //TODO
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
}
