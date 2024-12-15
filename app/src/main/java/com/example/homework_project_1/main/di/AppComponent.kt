package com.example.homework_project_1.main.di

import android.content.Context
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.di.module.DataModule
import com.example.homework_project_1.main.di.module.PresentationModule
import com.example.homework_project_1.main.di.module.RepositoryModule
import com.example.homework_project_1.main.di.module.UseCasesModule
import com.example.homework_project_1.main.di.module.ViewModelModule
import com.example.homework_project_1.main.di.module.WorkerModule
import com.example.homework_project_1.main.presentation.joke_add.AddJokeActivity
import com.example.homework_project_1.main.presentation.joke_details.JokeDetailsFragment
import com.example.homework_project_1.main.presentation.joke_list.JokeListActivity
import com.example.homework_project_1.main.presentation.joke_list.JokeListFragment
import com.example.homework_project_1.main.presentation.joke_list.JokeListViewModel
import com.example.homework_project_1.main.presentation.main_menu.MainMenuFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DataModule::class,
    ViewModelModule::class,
    RepositoryModule::class,
    WorkerModule::class,
    PresentationModule::class,
    UseCasesModule::class
])
interface AppComponent {
    fun inject(activity: JokeListActivity)
    fun inject(viewModel: JokeListViewModel)
    fun inject(fragment: JokeListFragment)
    fun inject(fragment: JokeDetailsFragment)
    fun inject(fragment: MainMenuFragment)
    fun inject(activity: AddJokeActivity)
    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}