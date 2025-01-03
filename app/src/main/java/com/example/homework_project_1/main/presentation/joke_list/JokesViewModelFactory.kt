package com.example.homework_project_1.main.presentation.joke_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

// Фабрика для создания ViewModel
@Suppress("UNCHECKED_CAST")
class JokesViewModelFactory @Inject constructor(
    private val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewModelMap[modelClass]
            ?: throw IllegalArgumentException("Unknown ViewModel class")
        return creator.get() as T
    }
}