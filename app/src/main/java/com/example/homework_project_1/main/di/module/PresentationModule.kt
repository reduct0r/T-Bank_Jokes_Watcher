package com.example.homework_project_1.main.di.module

import com.example.homework_project_1.main.presentation.utils.ViewTyped
import dagger.Module

@Module
class PresentationModule {

    fun provideUIModelFactory(): ViewTyped.JokeUIModel.JokeUIModelFactory {
        return ViewTyped.JokeUIModel.JokeUIModelFactory()
    }
}