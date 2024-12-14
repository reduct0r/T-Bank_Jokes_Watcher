package com.example.homework_project_1.main

import android.app.Application
import com.example.homework_project_1.main.di.AppComponent
import com.example.homework_project_1.main.di.DaggerAppComponent


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