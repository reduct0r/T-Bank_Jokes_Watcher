package com.example.homework_project_1.main.presentation.joke_list

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.domain.generator.JokesGenerator
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.convertToUIModel
import com.example.homework_project_1.main.data.utils.unique
import com.example.homework_project_1.main.di.module.ApiRepository
import com.example.homework_project_1.main.di.module.CacheRepository
import com.example.homework_project_1.main.di.module.JokesRepository
import com.example.homework_project_1.main.domain.repository.Repository
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

    @ApiRepository private val fetchRandomJokesFromApi: FetchRandomJokesFromApi,
    @JokesRepository private val insertJokeUseCase: InsertJokeUseCase,
    @CacheRepository private val insertCacheJokeUseCase:InsertJokeUseCase,
    @JokesRepository private val getAmountOfJokesUseCase: GetAmountOfJokesUseCase,
    @JokesRepository private val resetUsedJokesUseCase: ResetUsedJokesUseCase,
    @CacheRepository private val resetUsedCacheUseCase: ResetUsedJokesUseCase,
    @JokesRepository private val fetchRandomJokesFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @CacheRepository private val fetchRandomCacheFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @JokesRepository private val getUserJokesAfterUseCase: GetUserJokesAfterUseCase,


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
            // Удаление кэша через сутки
//            if (CacheRepositoryImpl.deleteDeprecatedCache(System.currentTimeMillis() - 3600000 * 24))
//                Toast.makeText(App.instance, "Deprecated cache cleared", Toast.LENGTH_SHORT).show()

            //TODO: для тестов сбросить состояние таблицы
//            RepositoryImpl.dropJokesTable()
//            RepositoryImpl.resetJokesSequence()

            //TODO: для тестов использованные шутки сбрасываются каждый запуск
//            withContext(Dispatchers.Default) {
//                RepositoryImpl.resetUsedJokes()
//                RepositoryImpl.resetCachedJokes()
//            }


            // Если в БД нет шуток (при первом запуске), то генерируем случайные
            if (getAmountOfJokesUseCase() == 0) {
                try {
                    JokesGenerator.generateJokesData(35)
                        .forEach { insertJokeUseCase(it) }
                    generateJokes()
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
        flag = false
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                var data = fetchRandomJokesFromDbUseCase(10)

                if (data.isNotEmpty()) {
                    data = JokesGenerator.setAvatar(data)
                    val uiModel = data.convertToUIModel(false)
                    _jokes.postValue(uiModel)
                } else {
                    _error.value = "There is no new jokes"
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
                newJokes = JokesGenerator.setAvatar(newJokes)
                val uiModels = newJokes.convertToUIModel(false)
                val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                _jokes.postValue(updatedJokes)
                _isRetryNeed.postValue(false)
            } catch (e: Exception) {
                var newJokes = fetchRandomCacheFromDbUseCase(5)
                if (newJokes.isNotEmpty()) {
                    _error.value = "Check Network connection"
                    newJokes = JokesGenerator.setAvatar(newJokes)
                    val uiModels = newJokes.convertToUIModel(false)
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
//        lastTimestamp = System.currentTimeMillis()
//        savedLastTimestamp = System.currentTimeMillis()
        JokesGenerator.reset()
    }
    private var flag: Boolean = false

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

                            newModels = JokesGenerator.setAvatar(newModels)

                            // Объединяем новые шутки с существующими, избегая дубликатов
                            val updatedJokes = (newModels.convertToUIModel(false) + (_jokes.value ?: emptyList()))
                                .distinctBy { it }

                            _jokes.postValue(updatedJokes)

                            //savedLastTimestamp = 0
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
}
