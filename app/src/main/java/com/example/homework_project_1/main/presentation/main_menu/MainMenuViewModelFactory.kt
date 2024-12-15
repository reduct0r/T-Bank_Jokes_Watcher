package com.example.homework_project_1.main.presentation.main_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import javax.inject.Inject
import javax.inject.Provider


@Suppress("UNCHECKED_CAST")
class MainMenuViewModelFactory @Inject constructor(
    private val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val creator = viewModelMap[modelClass]
            ?: throw IllegalArgumentException("Unknown ViewModel class")
        return creator.get() as T
    }
}