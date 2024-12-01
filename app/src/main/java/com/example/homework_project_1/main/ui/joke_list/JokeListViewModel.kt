package com.example.homework_project_1.main.ui.joke_list

import android.database.sqlite.SQLiteConstraintException
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
import com.example.homework_project_1.main.data.repository.RepositoryImpl
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

    private val _isLoadingAdded = MutableLiveData(false)
    val isLoadingAdded: LiveData<Boolean> get() = _isLoadingAdded

    fun setLoadingAdded(isAdded: Boolean) {
        _isLoadingAdded.value = isAdded
    }
    private var jokeObserver: Observer<List<JokeDTO>>? = null

    init {
        _isLoadingEl.value = false
        generateJokes()
        observeNewJoke()

    }

    fun generateJokes() {
        if (_isLoadingEl.value == true) return
        viewModelScope.launch {
            //RepositoryImpl.fetchDbJoke(1)
            _isLoading.postValue(true)
            try {
                //RepositoryImpl.dropJokesTable()
                //RepositoryImpl.resetJokesSequence()

                //val data = JokesGenerator.generateJokesData(1)
                var data = RepositoryImpl.fetchRandomDbJokes(2)
                data = JokesGenerator.setAvatar(data)

                val uiModel = data.convertToUIModel(false)
                _jokes.postValue(uiModel)

//                data.forEach { joke ->
//                    RepositoryImpl.insertDbJoke(joke)
//                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error while generating jokes."
                Log.e("mylog", "Error while generating jokes", e)
            } finally {
                _isLoading.postValue(false)

//                for (i in 1..6) {
//                    Log.d("myLog", RepositoryImpl.fetchDbJoke(i).toString())
//                }
            }
        }
    }

    fun loadMoreJokes() {
        if (_isLoadingEl.value == true) return

        viewModelScope.launch {
            _isLoadingEl.value = true
            try {
                var newJokes = RepositoryImpl.fetchApiJokes(amount = 10)

//                newJokes.forEach { joke ->
//                    RepositoryImpl.insertDbJoke(joke)
//                }

                newJokes = JokesGenerator.setAvatar(newJokes)

                val uiModels = newJokes.convertToUIModel(false)

                val updatedJokes = (_jokes.value ?: emptyList()) + uiModels
                _jokes.postValue(updatedJokes)

                newJokes.forEach { joke -> JokesGenerator.addToSelectedJokes(joke, index = -1) }
            } catch (e: Exception) {
                _error.value =  "Unknown error occurred while loading more jokes."
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
        RepositoryImpl.resetUsedJokes()
        JokesGenerator.reset()
    }

    // Наблюдение за добавлением новых шуток
    private fun observeNewJoke() {
        jokeObserver = Observer { newJokes ->
            if (newJokes.isNotEmpty()) {
                val lastJoke = newJokes.last()
                val modelUI = lastJoke.convertToUIModel(false)
                val updatedJokes = listOf(modelUI) + (_jokes.value ?: emptyList())
                _jokes.value = updatedJokes

            }

            //JokesRepository.getUserJokes().observeForever(jokeObserver!!)
        }
        RepositoryImpl.getUserJokesLiveData().observeForever(jokeObserver!!)
    }

    override fun onCleared() {
        super.onCleared()
        jokeObserver?.let {
            RepositoryImpl.getUserJokesLiveData().removeObserver(it)
        }
    }
}

