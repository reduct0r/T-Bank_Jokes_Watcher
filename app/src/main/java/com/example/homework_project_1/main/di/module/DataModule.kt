package com.example.homework_project_1.main.di.module

import android.app.Application
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.api.ApiServiceImpl
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import com.example.homework_project_1.main.data.repository.CacheRepositoryImpl
import com.example.homework_project_1.main.data.utils.JsonReader
import com.example.homework_project_1.main.di.annotations.CacheRepository
import com.example.homework_project_1.main.di.DaggerWorkerFactory
import com.example.homework_project_1.main.domain.generator.JokesGenerator
import com.example.homework_project_1.main.domain.repository.Repository
import com.example.homework_project_1.main.presentation.joke_add.AddJokeWorker
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
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

    @Singleton
    @Provides
    fun provideJokesGenerator(jsonReader: JsonReader): JokesGenerator {
        return JokesGenerator(jsonReader)
    }

    @Singleton
    @Provides
    fun provideDaggerWorkerFactory(addJokeWorkerFactory: AddJokeWorker.Factory): DaggerWorkerFactory {
        return DaggerWorkerFactory(addJokeWorkerFactory)
    }

    @Singleton
    @Provides
    fun provideApiServiceImpl(apiClient: HttpClient): ApiServiceImpl {
        return ApiServiceImpl.getInstance(apiClient)
    }

    @Singleton
    @CacheRepository
    @Provides
    fun provideCacheRepositoryImpl(jokeDb: JokesWatcherDatabase): Repository {
        return CacheRepositoryImpl(jokeDb)
    }

    @Singleton
    @Provides
    fun provideJokesWatcherDatabase(): JokesWatcherDatabase {
        return JokesWatcherDatabase.getInstance(App.instance)
    }
}