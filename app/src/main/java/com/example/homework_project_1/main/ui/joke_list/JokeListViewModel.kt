package com.example.homework_project_1.main.ui.joke_list

import android.util.Log
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
            Log.d("mylog generateJokes()", "Loading")
            _isLoading.postValue(true)
            try {
                val data = JokesGenerator.generateJokesData()

                val uiModel = data.convertToUIModel(false)
                _jokes.postValue(uiModel)
                Log.d("mylog generateJokes()", "Loaded")
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun loadMoreJokes() {
        if (_isLoadingEl.value == true) return

        viewModelScope.launch {
            Log.d("mylog loadMoreJokes()", "Loading")
            _isLoadingEl.value = true
            try {
                var newJokes = JokeRepositoryImpl.fetchJokes(amount = 5)
                //delay(1000)

                newJokes = JokesGenerator.setAvatar(newJokes)

                val uiModels = newJokes.convertToUIModel(false)

                val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                _jokes.postValue(updatedJokes)

                newJokes.forEach { joke -> JokesGenerator.addToSelectedJokes(joke, index = -1) }
                Log.d("mylog loadMoreJokes()", "Loaded")
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
        //_jokes.value = emptyList()
        JokesGenerator.reset()
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
