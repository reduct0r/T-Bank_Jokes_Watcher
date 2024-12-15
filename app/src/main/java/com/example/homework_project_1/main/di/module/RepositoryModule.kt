package com.example.homework_project_1.main.di.module

import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.repository.ApiRepositoryImpl
import com.example.homework_project_1.main.data.repository.JokesRepositoryImpl
import com.example.homework_project_1.main.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
 class RepositoryModule {

    @JokesRepository
    @Provides
    fun provideJokeRepositoryImpl(jokeDb: JokesWatcherDatabase): JokesRepositoryImpl {
        return JokesRepositoryImpl(jokeDb)
    }

    @Singleton
    @JokesRepository
    @Provides
    fun provideJokeRepository(jokeDb: JokesWatcherDatabase): Repository {
        return JokesRepositoryImpl(jokeDb)
    }

    @Singleton
    @ApiRepository
    @Provides
    fun provideApiRepositoryImpl(apiServiceImpl: ApiServiceImpl): Repository {
        return ApiRepositoryImpl(apiServiceImpl)
    }
}