package com.example.homework_project_1.main.di.module

import androidx.work.WorkerFactory
import com.example.homework_project_1.main.di.DaggerWorkerFactory
import dagger.Binds
import dagger.Module

@Module
abstract class WorkerModule {
    @Binds
    abstract fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory
}