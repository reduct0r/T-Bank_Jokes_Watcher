package com.example.homework_project_1.main.di.module

import androidx.lifecycle.ViewModel
import com.example.homework_project_1.main.di.annotations.ViewModelKey
import com.example.homework_project_1.main.presentation.joke_list.JokeListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(JokeListViewModel::class)
    abstract fun bindJokeListViewModel(viewModel: JokeListViewModel): ViewModel

}