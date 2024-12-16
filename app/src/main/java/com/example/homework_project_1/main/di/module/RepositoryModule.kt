package com.example.homework_project_1.main.di.module

import com.example.homework_project_1.main.data.repository.ApiRepositoryImpl
import com.example.homework_project_1.main.data.repository.CacheRepositoryImpl
import com.example.homework_project_1.main.data.repository.JokesRepositoryImpl
import com.example.homework_project_1.main.di.annotations.ApiRepositoryA
import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.repository.CacheRepository
import com.example.homework_project_1.main.domain.repository.JokesRepository
import com.example.homework_project_1.main.domain.repository.Repository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    @ApiRepositoryA
    fun bindCacheRepository(
        cacheRepositoryImpl: CacheRepositoryImpl
    ): CacheRepository

    @Binds
    @Singleton
    @ApiRepositoryA
    fun bindApiRepository(
        apiRepositoryImpl: ApiRepositoryImpl
    ): Repository

    @Binds
    @Singleton
    @JokesRepositoryA
    fun bindJokesRepository(
        jokesRepositoryImpl: JokesRepositoryImpl
    ): JokesRepository
}