package com.example.homework_project_1.main.ui.joke_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.convertToUiModel
import kotlinx.coroutines.launch

class JokeListViewModel : ViewModel() {
    private val _jokes = MutableLiveData<List<ViewTyped.JokeUIModel>>()
    val jokes: LiveData<List<ViewTyped.JokeUIModel>> = _jokes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun generateJokes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = JokesGenerator.generateJokesData()
                val uiModel = convertToUiModel(data, false)
                _jokes.value = uiModel
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun showGeneratedData(): List<ViewTyped.JokeUIModel> {
        return jokes.value ?: emptyList()
    }

    fun resetJokes() {
        JokesGenerator.reset()
    }
}