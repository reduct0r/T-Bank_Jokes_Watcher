package com.example.homework_project_1.main.presentation.main_menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.di.annotations.CacheRepositoryA
import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.generator.JokesGenerator
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import com.example.homework_project_1.main.domain.usecase.GetAmountOfJokesUseCase
import com.example.homework_project_1.main.domain.usecase.InsertJokeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainMenuViewModel @Inject constructor(
    @JokesRepositoryA private val fetchRandomJokesFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @CacheRepositoryA private val fetchRandomCacheFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @JokesRepositoryA private val getAmountOfJokesUseCase: GetAmountOfJokesUseCase,
    @JokesRepositoryA private val insertJokeUseCase: InsertJokeUseCase,
    private val jokesGenerator: JokesGenerator,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _textLine = MutableLiveData<String>()
    val textLine: LiveData<String> get() = _textLine

    private var lines: List<String> = emptyList()
    private var currentIndex = 0

    private var isRunning = false

    fun startUpdatingLines() {
        if (isRunning) return
        isRunning = true
        viewModelScope.launch {
            _loading.postValue(true)

            if (getAmountOfJokesUseCase() == 0) {
                try {
                    val generatedJokes = jokesGenerator.generateJokesData(35)
                    generatedJokes.forEach { insertJokeUseCase(it) }
                } catch (e: Exception) {
                    Log.e("MainMenuViewModel", "Failed to generate jokes", e)
                }
            }

            lines = withContext(Dispatchers.IO) {
                (fetchRandomJokesFromDbUseCase(20, false)
                        + fetchRandomCacheFromDbUseCase(20, false)).map {
                            "${it.question}\n— ${it.answer}"
                        }
            }
            _loading.postValue(false)

            while (isRunning) {
                if (lines.isNotEmpty()) {
                    _textLine.postValue(lines[currentIndex])
                    currentIndex = (currentIndex + 1) % lines.size
                } else {
                    _textLine.postValue("Nothing to show")
                }
                delay(6000)
            }
        }
    }
}
