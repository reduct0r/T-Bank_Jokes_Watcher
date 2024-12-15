package com.example.homework_project_1.main.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class JokesRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiRepository