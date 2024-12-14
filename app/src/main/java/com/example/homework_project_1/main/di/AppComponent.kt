package com.example.homework_project_1.main.di

import android.content.Context
import com.example.homework_project_1.main.di.module.DataModule
import com.example.homework_project_1.main.presentation.joke_list.JokeListActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {
    fun inject(activity: JokeListActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}