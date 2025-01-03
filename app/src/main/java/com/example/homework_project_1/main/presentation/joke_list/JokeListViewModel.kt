package com.example.homework_project_1.main.presentation.joke_list

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.domain.generator.JokesGenerator
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.convertToUIModel
import com.example.homework_project_1.main.data.utils.unique
import com.example.homework_project_1.main.di.annotations.ApiRepositoryA
import com.example.homework_project_1.main.di.annotations.CacheRepositoryA
import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.usecase.AddToFavouritesUseCase
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromApi
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import com.example.homework_project_1.main.domain.usecase.GetAmountOfJokesUseCase
import com.example.homework_project_1.main.domain.usecase.GetUserJokesAfterUseCase
import com.example.homework_project_1.main.domain.usecase.InsertJokeUseCase
import com.example.homework_project_1.main.domain.usecase.ResetUsedJokesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JokeListViewModel @Inject constructor(
    @ApiRepositoryA private val fetchRandomJokesFromApi: FetchRandomJokesFromApi,
    @JokesRepositoryA private val insertJokeUseCase: InsertJokeUseCase,
    @CacheRepositoryA private val insertCacheJokeUseCase:InsertJokeUseCase,
    @JokesRepositoryA private val getAmountOfJokesUseCase: GetAmountOfJokesUseCase,
    @JokesRepositoryA private val resetUsedJokesUseCase: ResetUsedJokesUseCase,
    @CacheRepositoryA private val resetUsedCacheUseCase: ResetUsedJokesUseCase,
    @JokesRepositoryA private val fetchRandomJokesFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @CacheRepositoryA private val fetchRandomCacheFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @JokesRepositoryA private val getUserJokesAfterUseCase: GetUserJokesAfterUseCase,
    @JokesRepositoryA private val addToFavouritesUseCase: AddToFavouritesUseCase,

    private val jokesGenerator: JokesGenerator,
    ): ViewModel() {

    private val _jokes = MutableLiveData<List<ViewTyped.JokeUIModel>>()
    val jokes: LiveData<List<ViewTyped.JokeUIModel>> = _jokes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingEl = MutableLiveData<Boolean>()
    val isLoadingEl: LiveData<Boolean> = _isLoadingEl

    private val _isLoadingAdded = MutableLiveData(false)
    val isLoadingAdded: LiveData<Boolean> get() = _isLoadingAdded

    private val _isRetryNeed = MutableLiveData<Boolean>()
    val isRetryNeed: LiveData<Boolean> = _isRetryNeed

    private val sharedPreferences: SharedPreferences = App.instance.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private var savedLastTimestamp = sharedPreferences.getLong("lastTimestamp", System.currentTimeMillis())
    private var lastTimestamp = savedLastTimestamp - 2

    fun setLoadingAdded(isAdded: Boolean) {
        _isLoadingAdded.value = isAdded
    }

    init {
        _isLoadingEl.value = false
        observeNewJoke()

        viewModelScope.launch {
            // Если в БД нет шуток (при первом запуске), то генерируем случайные
            if (getAmountOfJokesUseCase() == 0) {
                try {
                    jokesGenerator.generateJokesData(35)
                        .forEach { insertJokeUseCase(it) }
                    generateJokes()
                    Log.d("JokeListViewModel", "Jokes generated")
                } catch (e: Exception) {
                    // игнорируем повторения шуток
                }
            }
            else {
                if (fetchRandomJokesFromDbUseCase(1).isEmpty()){
                    resetUsedJokesUseCase()
                }
                generateJokes()
            }

        }
    }

    fun generateJokes() {
        if (_isLoadingEl.value == true) return
        editor.putLong("lastTimestamp", System.currentTimeMillis()).apply()
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                var data = fetchRandomJokesFromDbUseCase(10)

                if (data.isNotEmpty()) {
                    data = jokesGenerator.setAvatar(data)
                    val uiModel = data.convertToUIModel()
                    _jokes.postValue(uiModel)
                } else {
                    _jokes.postValue(emptyList())
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error while generating jokes."
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun loadMoreJokes() {
        if (_isLoadingEl.value == true) return
        viewModelScope.launch {
            _isLoadingEl.value = true
            try {
                var newJokes = fetchRandomJokesFromApi(5)
                newJokes.forEach { joke ->
                    insertCacheJokeUseCase(joke)
                }
                newJokes = jokesGenerator.setAvatar(newJokes)
                val uiModels = newJokes.convertToUIModel()
                val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                _jokes.postValue(updatedJokes)
                _isRetryNeed.postValue(false)
            } catch (e: Exception) {
                var newJokes = fetchRandomCacheFromDbUseCase(5)
                if (newJokes.isNotEmpty()) {
                    _error.value = "Check Network connection"
                    newJokes = jokesGenerator.setAvatar(newJokes)
                    val uiModels = newJokes.convertToUIModel()
                    val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                    _jokes.postValue(updatedJokes)
                } else {
                    _isRetryNeed.postValue(true)
                    _error.postValue("Unknown error occurred while loading more jokes.")
                }
            } finally {
                _isLoadingEl.value = false
            }
        }
    }

    fun resetJokes() {
        //_jokes.value = emptyList()
        _isLoading.value = false
        _isLoadingEl.value = false
        _isLoadingAdded.value = false
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                resetUsedJokesUseCase()
                resetUsedCacheUseCase()
            }
        }
        jokesGenerator.reset()
    }

    private fun observeNewJoke() {
        viewModelScope.launch {
            try {
                getUserJokesAfterUseCase(lastTimestamp + 1)
                    .unique()
                    .collect { newJokes ->
                        if (newJokes.isNotEmpty()) {
                            val sortedJokes = newJokes.sortedBy { it.createdAt }.filter { it.createdAt > lastTimestamp }
                            var newModels = sortedJokes
                                .map { it.toDto()}

                            newModels = jokesGenerator.setAvatar(newModels)

                            // Объединяем новые шутки с существующими, избегая дубликатов
                            val updatedJokes = (newModels.convertToUIModel() + (_jokes.value ?: emptyList()))
                                .distinctBy { it }

                            _jokes.postValue(updatedJokes)

                            lastTimestamp = sortedJokes.maxOf { it.createdAt} +1
                            savedLastTimestamp = sortedJokes.minOf { it.createdAt}
                            editor.putLong("lastTimestamp", savedLastTimestamp).apply()
                        }
                    }

            } catch (e: Exception) {
                _error.postValue(e.message ?: "Unknown error occurred while observing new jokes.")
            }
        }
    }

    suspend fun toggleFavorite(joke: ViewTyped.JokeUIModel) {
        joke.isFavorite = !joke.isFavorite
        addToFavouritesUseCase(joke)
    }
}
