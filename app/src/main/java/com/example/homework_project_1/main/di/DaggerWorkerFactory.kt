package com.example.homework_project_1.main.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.homework_project_1.main.presentation.joke_add.AddJokeWorker
import javax.inject.Inject

class DaggerWorkerFactory @Inject constructor(
    private val addJokeWorkerFactory: AddJokeWorker.Factory
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            AddJokeWorker::class.java.name -> addJokeWorkerFactory.create(appContext, workerParameters)
            else -> null
        }
    }
}