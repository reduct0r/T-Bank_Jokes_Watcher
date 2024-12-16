package com.example.homework_project_1.main.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class JokesRepositoryA

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheRepositoryA

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiRepositoryA