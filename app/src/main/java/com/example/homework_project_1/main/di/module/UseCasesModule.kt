package com.example.homework_project_1.main.di.module

import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.di.annotations.ApiRepositoryA
import com.example.homework_project_1.main.di.annotations.CacheRepositoryA
import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.repository.JokesRepository
import com.example.homework_project_1.main.domain.repository.Repository
import com.example.homework_project_1.main.domain.usecase.AddToFavouritesUseCase
import com.example.homework_project_1.main.domain.usecase.DeleteDeprecatedCacheUseCase
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromApi
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import com.example.homework_project_1.main.domain.usecase.GetAmountOfJokesUseCase
import com.example.homework_project_1.main.domain.usecase.GetFavoriteJokesUseCase
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
    @JokesRepositoryA
    fun provideGetUserJokesAfterUseCase(@JokesRepositoryA repository: JokesRepository): GetUserJokesAfterUseCase {
        return GetUserJokesAfterUseCase(repository)
    }

    @Singleton
    @Provides
    @ApiRepositoryA
    fun provideFetchRandomJokesFromApiUseCase(@ApiRepositoryA repository: Repository): FetchRandomJokesFromApi {
        return FetchRandomJokesFromApi(repository)
    }

    @Singleton
    @Provides
    @JokesRepositoryA
    fun provideFetchRandomJokesFromDbUseCase(@JokesRepositoryA repository: JokesRepository): FetchRandomJokesFromDbUseCase {
        return FetchRandomJokesFromDbUseCase(repository)
    }

    @Singleton
    @Provides
    @CacheRepositoryA
    fun provideFetchRandomCacheJokesFromDbUseCase(@CacheRepositoryA repository: Repository): FetchRandomJokesFromDbUseCase {
        return FetchRandomJokesFromDbUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepositoryA
    fun provideGetAmountOfJokesUseCase(@JokesRepositoryA repository: JokesRepository): GetAmountOfJokesUseCase {
        return GetAmountOfJokesUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepositoryA
    fun provideResetUsedJokesUseCase(@JokesRepositoryA repository: JokesRepository): ResetUsedJokesUseCase {
        return ResetUsedJokesUseCase(repository)
    }

    @Singleton
    @Provides
    @CacheRepositoryA
    fun provideResetUsedJokesCacheUseCase(@CacheRepositoryA repository: Repository): ResetUsedJokesUseCase {
        return ResetUsedJokesUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepositoryA
    fun provideInsertJokeUseCaseJokes(@JokesRepositoryA repository: JokesRepository): InsertJokeUseCase {
        return InsertJokeUseCase(repository)
    }

    @Singleton
    @Provides
    @CacheRepositoryA
    fun provideInsertJokeUseCaseCache(@CacheRepositoryA repository: Repository): InsertJokeUseCase {
        return InsertJokeUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepositoryA
    fun provideAddToFavouritesUseCase(@JokesRepositoryA repository: JokesRepository): AddToFavouritesUseCase {
        return AddToFavouritesUseCase(repository)
    }

    @Singleton
    @Provides
    @JokesRepositoryA
    fun provideGetFavoriteJokesUseCase(@JokesRepositoryA repository: JokesRepository): GetFavoriteJokesUseCase {
        return GetFavoriteJokesUseCase(repository)
    }

}