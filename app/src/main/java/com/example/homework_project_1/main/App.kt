package com.example.homework_project_1.main

import android.app.Application
import androidx.work.Configuration
import com.example.homework_project_1.main.di.AppComponent
import com.example.homework_project_1.main.di.DaggerAppComponent
import com.example.homework_project_1.main.di.DaggerWorkerFactory
import javax.inject.Inject

class App : Application(), Configuration.Provider {
    lateinit var appComponent: AppComponent
        private set

    @Inject
    lateinit var daggerWorkerFactory: DaggerWorkerFactory

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
    }

    override val workManagerConfiguration: Configuration by lazy {
        Configuration.Builder()
            .setWorkerFactory(daggerWorkerFactory)
            .build()
    }
}
