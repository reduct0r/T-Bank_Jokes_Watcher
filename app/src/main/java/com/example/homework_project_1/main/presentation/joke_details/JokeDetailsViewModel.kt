package com.example.homework_project_1.main.presentation.joke_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.domain.usecase.AddToFavouritesUseCase
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class JokeDetailsViewModel @AssistedInject constructor(
    @Assisted private val gotJoke: ViewTyped.JokeUIModel,
    private val addToFavouritesUseCase: AddToFavouritesUseCase
) : ViewModel() {

    private val _joke = MutableLiveData<ViewTyped.JokeUIModel>()
    val joke: LiveData<ViewTyped.JokeUIModel> get() = _joke

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    init {
        loadJoke()
    }

    fun loadJoke() {
        _joke.value = gotJoke
        _isFavorite.value = gotJoke.isFavorite
    }

    fun addToFavorites() {
        viewModelScope.launch {
            try {
                gotJoke.isFavorite = true
                _isFavorite.value = true
                addToFavouritesUseCase(gotJoke)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch {
            try {
                gotJoke.isFavorite = false
                _isFavorite.value = false
                addToFavouritesUseCase(gotJoke)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(gotJoke: ViewTyped.JokeUIModel): JokeDetailsViewModel
    }
}