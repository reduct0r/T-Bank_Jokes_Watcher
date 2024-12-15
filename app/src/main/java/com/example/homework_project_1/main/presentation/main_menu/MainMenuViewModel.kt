package com.example.homework_project_1.main.presentation.main_menu

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.main.di.annotations.CacheRepository
import com.example.homework_project_1.main.di.annotations.JokesRepository
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainMenuViewModel @Inject constructor(
    @JokesRepository private val fetchRandomJokesFromDbUseCase: FetchRandomJokesFromDbUseCase,
    @CacheRepository private val fetchRandomCacheFromDbUseCase: FetchRandomJokesFromDbUseCase,
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
            if (lines.isEmpty()) {
                _loading.postValue(true)
                lines = withContext(Dispatchers.IO) {
                    (fetchRandomJokesFromDbUseCase(20, false) + fetchRandomCacheFromDbUseCase(20, false))
                        .map { it.question + " \n " + it.answer }
                }
                _loading.postValue(false)
            }

            while (isRunning) {
                if (lines.isNotEmpty()) {
                    _textLine.postValue(lines[currentIndex])
                    currentIndex = (currentIndex + 1) % lines.size
                } else {
                    _textLine.postValue("No jokes in database")
                }
                delay(5000)
            }
        }
    }

    fun stopUpdatingLines() {
        isRunning = false
    }
}