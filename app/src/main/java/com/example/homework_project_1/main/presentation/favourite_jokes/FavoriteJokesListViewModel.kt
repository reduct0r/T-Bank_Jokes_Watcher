package com.example.homework_project_1.main.presentation.favourite_jokes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.convertToUIModel
import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.usecase.AddToFavouritesUseCase
import com.example.homework_project_1.main.domain.usecase.GetFavoriteJokesUseCase
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteJokesListViewModel @Inject constructor(
    @JokesRepositoryA private val addToFavouritesUseCase: AddToFavouritesUseCase,
    @JokesRepositoryA private val getFavoriteJokesUseCase: GetFavoriteJokesUseCase
) : ViewModel() {

    private val _favoriteJokes = MutableLiveData<List<ViewTyped.JokeUIModel>>()
    val favoriteJokes: LiveData<List<ViewTyped.JokeUIModel>> get() = _favoriteJokes

    init {
        loadFavoriteJokes()
        Log.d("FavoriteJokesListViewModel", "init: ")
    }

    fun loadFavoriteJokes() {
        viewModelScope.launch {
            val jokes = getFavoriteJokesUseCase()
            Log.d("FavoriteJokesListViewModel", "loadFavoriteJokes: $jokes")
            _favoriteJokes.postValue(jokes.convertToUIModel())
        }
    }

    fun toggleFavorite(joke: ViewTyped.JokeUIModel) {
        viewModelScope.launch {
            addToFavouritesUseCase(joke)
            loadFavoriteJokes()
        }
    }
}