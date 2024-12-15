package com.example.homework_project_1.main.di.module

import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.di.annotations.ApiRepository
import com.example.homework_project_1.main.di.annotations.CacheRepository
import com.example.homework_project_1.main.di.annotations.JokesRepository
import com.example.homework_project_1.main.domain.repository.Repository
import com.example.homework_project_1.main.domain.usecase.AddToFavouritesUseCase
import com.example.homework_project_1.main.domain.usecase.DeleteDeprecatedCacheUseCase
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromApi
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import com.example.homework_project_1.main.domain.usecase.GetAmountOfJokesUseCase
import com.example.homework_project_1.main.domain.usecase.GetUserJokesAfterUseCase
import com.example.homework_project_1.main.domain.usecase.InsertJokeUseCase
import com.example.homework_project_1.main.domain.usecase.ResetUsedJokesUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCasesModule {

    @Provides
    fun provideDeleteDeprecatedCacheUseCase(jokeDb: JokesWatcherDatabase): DeleteDeprecatedCacheUseCase {
        return DeleteDeprecatedCacheUseCase(jokeDb)
    }

    @Singleton
    @Provides
    @JokesRepository
    fun provideGetUserJokesAfterUseCase(@JokesRepository repository: Repository): GetUserJokesAfterUseCase {
        return GetUserJokesAfterUseCase(repository)
    }

    @Singleton
    @Provides
    @ApiRepository
    fun provideFetchRandomJokesFromApiUseCase(@ApiRepository repository: Repository): FetchRandomJokesFromApi {
        return FetchRandomJokesFromApi(repository)
    }

    @Singleton
    @Provides
    @JokesRepository
    fun provideFetchRandomJokesFromDbUseCase(@JokesRepository repository: Repository): FetchRandomJokesFromDbUseCase {
        return FetchRandomJokesFromDbUseCase(repository)
    }

    @Singleton
    @Provides
    @CacheRepository
    fun provideFetchRandomCacheJokesFromDbUseCase(@CacheRepository repository: Repository): FetchRandomJokesFromDbUseCase {
        return FetchRandomJokesFromDbUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepository
    fun provideGetAmountOfJokesUseCase(@JokesRepository repository: Repository): GetAmountOfJokesUseCase {
        return GetAmountOfJokesUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepository
    fun provideResetUsedJokesUseCase(@JokesRepository repository: Repository): ResetUsedJokesUseCase {
        return ResetUsedJokesUseCase(repository)
    }

    @Singleton
    @Provides
    @CacheRepository
    fun provideResetUsedJokesCacheUseCase(@CacheRepository repository: Repository): ResetUsedJokesUseCase {
        return ResetUsedJokesUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepository
    fun provideInsertJokeUseCaseJokes(@JokesRepository repository: Repository): InsertJokeUseCase {
        return InsertJokeUseCase(repository)
    }

    @Singleton
    @Provides
    @CacheRepository
    fun provideInsertJokeUseCaseCache(@CacheRepository repository: Repository): InsertJokeUseCase {
        return InsertJokeUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideAddToFavouritesUseCase(jokeDb: JokesWatcherDatabase): AddToFavouritesUseCase {
        return AddToFavouritesUseCase(jokeDb)
    }
}