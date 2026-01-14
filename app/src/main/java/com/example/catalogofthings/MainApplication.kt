package com.example.catalogofthings

import android.app.Application
import android.content.Context
import com.example.catalogofthings.di.AppComponent
import com.example.catalogofthings.di.DaggerAppComponent

class MainApplication: Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        super.onCreate()
    }
}

val Context.appComponent: AppComponent
    get() = when(this) {
        is MainApplication -> this.appComponent
        else -> this.applicationContext.appComponent
    }