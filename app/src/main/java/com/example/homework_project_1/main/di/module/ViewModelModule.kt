package com.example.homework_project_1.main.di.module

import androidx.lifecycle.ViewModel
import com.example.homework_project_1.main.di.annotations.ViewModelKey
import com.example.homework_project_1.main.presentation.joke_details.JokeDetailsViewModel
import com.example.homework_project_1.main.presentation.joke_list.JokeListViewModel
import com.example.homework_project_1.main.presentation.main_menu.MainMenuViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(JokeListViewModel::class)
    abstract fun bindJokeListViewModel(viewModel: JokeListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainMenuViewModel::class)
    abstract fun bindMainMenuViewModel(viewModel: JokeListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JokeDetailsViewModel::class)
    abstract fun bindJokeDetailsViewModelFactory(factory: JokeDetailsViewModel.Factory): JokeDetailsViewModel.Factory

}