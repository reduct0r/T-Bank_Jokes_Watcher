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
import javax.inject.Inject

class JokeDetailsViewModel @AssistedInject constructor(
    @Assisted private val gotJoke: ViewTyped.JokeUIModel,
    private val addToFavouritesUseCase: AddToFavouritesUseCase
) : ViewModel() {

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
                gotJoke.isFavorite = true
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