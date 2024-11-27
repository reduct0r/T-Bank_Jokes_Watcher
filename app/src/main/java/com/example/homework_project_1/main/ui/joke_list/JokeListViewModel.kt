package com.example.homework_project_1.main.ui.joke_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.JokesRepository
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.convertToUIModel
import com.example.homework_project_1.main.data.repository.JokeRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class JokeListViewModel : ViewModel() {

    private val _jokes = MutableLiveData<List<ViewTyped.JokeUIModel>>()
    val jokes: LiveData<List<ViewTyped.JokeUIModel>> = _jokes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingEl = MutableLiveData<Boolean>()
    val isLoadingEl: LiveData<Boolean> = _isLoadingEl


    private var jokeObserver: Observer<List<JokeDTO>>? = null

    init {
        observeNewJoke()
    }

    fun generateJokes() {
        viewModelScope.launch {

            _isLoading.value = true
            try {
                val data = JokesGenerator.generateJokesData()

                val uiModel = data.convertToUIModel(false)
                _jokes.value = uiModel
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreJokes() {
        viewModelScope.launch {
            _isLoadingEl.value = true
            try {
                var newJokes = JokeRepositoryImpl.fetchJokes(amount = 5)
                delay(500)

                newJokes = JokesGenerator.setAvatar(newJokes)

                val uiModels = newJokes.convertToUIModel(false)

                val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                _jokes.value = updatedJokes

                newJokes.forEach { joke -> JokesGenerator.addToSelectedJokes(joke, index = -1) }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred while loading more jokes."
            } finally {
                _isLoadingEl.value = false
            }
        }
    }

    // Получение списка сгенерированных шуток
    fun getRenderedJokesList(): List<ViewTyped.JokeUIModel> {
        return _jokes.value ?: emptyList()
    }

    // Сброс показанных шуток
    fun resetJokes() {
        JokesGenerator.reset()
        //_jokes.value = emptyList()
    }

    // Наблюдение за добавлением новых шуток
    private fun observeNewJoke() {
        jokeObserver = Observer { newJokes ->
            if (newJokes.isNotEmpty()) {
                val lastJoke = newJokes.last()
                val modelUI = lastJoke.convertToUIModel(false)
                val updatedJokes = (_jokes.value ?: emptyList()) + modelUI
                _jokes.value = updatedJokes
            }
        }
        JokesRepository.getUserJokes().observeForever(jokeObserver!!)
    }

    override fun onCleared() {
        super.onCleared()
        jokeObserver?.let {
            JokesRepository.getUserJokes().removeObserver(it)
        }
    }
}
