package com.example.homework_project_1.main

import android.app.Application
import androidx.work.Configuration
import com.example.homework_project_1.main.di.AppComponent
import com.example.homework_project_1.main.di.DaggerAppComponent
import javax.inject.Inject


class App : Application() {
    lateinit var appComponent: AppComponent

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.factory().create(this)
    }

}