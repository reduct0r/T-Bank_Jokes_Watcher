package com.example.homework_project_1.main.ui.joke_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.JokesRepository
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

    init {
        observeNewJoke()
    }

    fun generateJokes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = JokesGenerator.generateJokesData()
                val uiModel = convertToUiModel(false, *data.toTypedArray())
                _jokes.value = uiModel
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreJokes() {
        if (_isLoading.value == true) return
        _isLoading.value = true
        viewModelScope.launch {
            JokesRepository.loadMoreJokes()
            _isLoading.value = false
        }
    }

    // Получение списка сгенерированных шуток
    fun getRenderedJokesList(): List<ViewTyped.JokeUIModel> {
        return jokes.value ?: emptyList()
    }

    // Сброс показанных шуток
    fun resetJokes() {
        JokesGenerator.reset()
    }

    // Наблюдение за добавлением новых шуток
    private fun observeNewJoke() {
        viewModelScope.launch {
            _isLoading.value = true

            JokesRepository.getUserJokes().observeForever { newJokes ->
                val lastJoke = newJokes.last()
                if (_jokes.value != null) {
                    val modelUI = convertToUiModel(false, lastJoke)
                    _jokes.value = _jokes.value?.plus(modelUI)
                } else {
                    _jokes.value = convertToUiModel(false, lastJoke)
                }
            }
            _isLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Отписываемся от наблюдателя, чтобы избежать утечек памяти
        JokesRepository.getUserJokes().removeObserver { }
    }
}
