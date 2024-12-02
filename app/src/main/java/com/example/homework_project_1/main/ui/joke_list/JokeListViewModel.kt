package com.example.homework_project_1.main.ui.joke_list

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.JokesRepository
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.model.JokeDTO.Companion.convertToUIModel
import com.example.homework_project_1.main.data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokeListViewModel : ViewModel() {

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

    private var lastTimestamp: Long = System.currentTimeMillis()

    fun setLoadingAdded(isAdded: Boolean) {
        _isLoadingAdded.value = isAdded
    }

    init {
        _isLoadingEl.value = false
        generateJokes()
        observeNewJoke()

        viewModelScope.launch {
            // Удаление кэша через сутки
            if (RepositoryImpl.deleteDeprecatedCache(System.currentTimeMillis() - 3600000 * 24))
            Toast.makeText(App.instance, "Deprecated cache cleared", Toast.LENGTH_SHORT).show()

            //TODO: для тестов сбросить состояние таблицы
//            RepositoryImpl.dropJokesTable()
//            RepositoryImpl.resetJokesSequence()

            //TODO: для тестов запись шуток в бд
            try {
                JokesGenerator.generateJokesData(35).forEach { RepositoryImpl.insertDbJoke(it) }
            } catch (e: Exception){
                Log.d("JokesGenerator", e.message.toString())
            }
            //TODO: для тестов использованные шутки сбрасываются каждый запуск
//            withContext(Dispatchers.Default) {
//                RepositoryImpl.resetUsedJokes()
//                RepositoryImpl.resetCachedJokes()
//            }
        }
    }

    fun generateJokes() {
        if (_isLoadingEl.value == true) return
        lastTimestamp = System.currentTimeMillis()
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                var data = RepositoryImpl.fetchRandomDbJokes(10)
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
                var newJokes = RepositoryImpl.fetchApiJokes(amount = 10)
                newJokes.forEach { joke ->
                    RepositoryImpl.insertCacheJoke(joke)
                }
                newJokes = JokesGenerator.setAvatar(newJokes)
                val uiModels = newJokes.convertToUIModel(false)
                val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                _jokes.postValue(updatedJokes)
                _isRetryNeed.postValue(false)
            } catch (e: Exception) {
                var newJokes = RepositoryImpl.fetchRandomCacheJokes(5)
                if (newJokes.size == 5) {
                    _error.value = "Check Network connection"
                    newJokes = JokesGenerator.setAvatar(newJokes)
                    val uiModels = newJokes.convertToUIModel(false)
                    val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                    _jokes.postValue(updatedJokes)
                } else {
                    RepositoryImpl.markCacheShown()
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
                RepositoryImpl.resetUsedJokes()
                RepositoryImpl.resetCachedJokes()
            }
        }
        lastTimestamp = System.currentTimeMillis()
        JokesGenerator.reset()
    }

    private fun observeNewJoke() {
        viewModelScope.launch {
            try {
                RepositoryImpl.getDbUserJokesAfter(lastTimestamp)
                    .collect { newJokes ->
                        if (newJokes.isNotEmpty()) {
                            val sortedJokes = newJokes.sortedBy { it.createdAt }

                            val newModels = sortedJokes
                                .map { it.toDto().convertToUIModel(false) }

                            // Объединяем новые шутки с существующими, избегая дубликатов
                            val updatedJokes = (newModels + (_jokes.value ?: emptyList()))
                                .distinctBy { it }

                            _jokes.postValue(updatedJokes)
                            RepositoryImpl.setMark(true, sortedJokes.map { it.id!! })

                            lastTimestamp = System.currentTimeMillis()
                        }
                    }
            } catch (e: Exception) {
                _error.postValue("Error access DataBase")
                Log.e("JokeViewModel", "Error observing new jokes", e)
            }
        }
    }

}
