package com.example.homework_project_1.main.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.api.ApiService
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.repository.ApiRepositoryImpl
import com.example.homework_project_1.main.data.repository.CacheRepositoryImpl
import com.example.homework_project_1.main.data.repository.JokesRepositoryImpl
import com.example.homework_project_1.main.data.utils.JsonReader
import com.example.homework_project_1.main.di.DaggerWorkerFactory
import com.example.homework_project_1.main.di.ViewModelKey
import com.example.homework_project_1.main.domain.generator.JokesGenerator
import com.example.homework_project_1.main.domain.repository.Repository
import com.example.homework_project_1.main.domain.usecase.DeleteDeprecatedCacheUseCase
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromApi
import com.example.homework_project_1.main.domain.usecase.FetchRandomJokesFromDbUseCase
import com.example.homework_project_1.main.domain.usecase.GetAmountOfJokesUseCase
import com.example.homework_project_1.main.domain.usecase.GetUserJokesAfterUseCase
import com.example.homework_project_1.main.domain.usecase.InsertJokeUseCase
import com.example.homework_project_1.main.domain.usecase.ResetUsedJokesUseCase
import com.example.homework_project_1.main.presentation.joke_add.AddJokeWorker
import com.example.homework_project_1.main.presentation.joke_list.JokeListViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.Multibinds
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Provider
import javax.inject.Singleton

@Module
class DataModule {

    fun provideHttpClient(): HttpClient {
        val httpClient: HttpClient by lazy {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
                install(Logging) {
                    level = LogLevel.BODY
                }
            }
        }
        return httpClient
    }

    @Provides
    @Singleton
    fun provideApp(application: Application): App {
        return application as App
    }

    @Provides
    @Singleton
    fun provideJsonReader(): JsonReader {
        return JsonReader()
    }

    @Provides
    fun provideJokesGenerator(jsonReader: JsonReader): JokesGenerator {
        return JokesGenerator(jsonReader)
    }


    @Provides
    fun provideDaggerWorkerFactory(addJokeWorkerFactory: AddJokeWorker.Factory): DaggerWorkerFactory {
        return DaggerWorkerFactory(addJokeWorkerFactory)
    }

    @JokesRepository
    @Provides
    fun provideJokeRepositoryImpl(jokeDb: JokesWatcherDatabase): JokesRepositoryImpl {
        return JokesRepositoryImpl(jokeDb)
    }
    @Provides
    fun provideApiServiceImpl(): ApiServiceImpl {
        return ApiServiceImpl.getInstance()
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

    @Singleton
    @CacheRepository
    @Provides
    fun provideCacheRepositoryImpl(jokeDb: JokesWatcherDatabase): Repository {
        return CacheRepositoryImpl(jokeDb)
    }

    @Singleton
    @Provides
    @ApiRepository
    fun provideFetchRandomJokesFromApi(@ApiRepository repository: Repository): FetchRandomJokesFromApi {
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
    @JokesRepository
    fun provideGetUserJokesAfterUseCase(@JokesRepository repository: Repository): GetUserJokesAfterUseCase {
        return GetUserJokesAfterUseCase(repository)
    }

    @Provides
    fun provideJokesWatcherDatabase(): JokesWatcherDatabase {
        return JokesWatcherDatabase.getInstance(App.instance)
    }

    @Provides
    fun provideDeleteDeprecatedCacheUseCase(jokeDb: JokesWatcherDatabase): DeleteDeprecatedCacheUseCase {
        return DeleteDeprecatedCacheUseCase(jokeDb)
    }

}