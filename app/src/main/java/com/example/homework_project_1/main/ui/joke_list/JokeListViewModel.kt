package com.example.homework_project_1.main.ui.joke_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.data.AvatarProvider
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.JokesRepository
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.convertToUiModel
import com.example.homework_project_1.main.data.repository.JokeRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JokeListViewModel : ViewModel() {

    private val _jokes = MutableLiveData<List<ViewTyped.JokeUIModel>>()
    val jokes: LiveData<List<ViewTyped.JokeUIModel>> = _jokes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingEl = MutableLiveData<Boolean>()
    val isLoadingEl: LiveData<Boolean> = _isLoadingEl

    init {
        observeNewJoke()
    }

    fun generateJokes() {
        if (_isLoadingEl.value == true) return
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

        _isLoadingEl.value = true
        viewModelScope.launch {
            try {
                val newJokes = JokeRepositoryImpl.fetchJokes(amount = 5)
                delay(500)

                newJokes.forEach { joke ->
                    if (joke.avatar == null && joke.avatarUri == null) {
                        val avatars = AvatarProvider.getAvatarsByCategory(joke.category)
                        val usedAvatars = mutableSetOf<Int>()
                        val availableAvatars = avatars.filter { it !in usedAvatars }
                        val selectedAvatar = if (availableAvatars.isNotEmpty()) {
                            availableAvatars.random()
                        } else {
                            AvatarProvider.getDefaultAvatars().random()
                        }
                        joke.avatar = selectedAvatar
                    }
                }

                val uiModels = convertToUiModel(false, *newJokes.toTypedArray())
                val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                _jokes.value = updatedJokes
                JokesGenerator.addToSelectedJokes(*newJokes.toTypedArray(), index = -1)
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
        JokesRepository.getUserJokes().observeForever { newJokes ->
            val lastJoke = newJokes.last()
            val modelUI = convertToUiModel(false, lastJoke)
            val updatedJokes = (_jokes.value ?: emptyList()) + modelUI
            _jokes.value = updatedJokes
        }
    }

    override fun onCleared() {
        super.onCleared()
        JokesRepository.getUserJokes().removeObserver { }
    }

}
