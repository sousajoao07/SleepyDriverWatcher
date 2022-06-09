package com.ssc.sleepyDriverWatcher

import android.app.Application
import android.content.Intent
import com.ssc.sleepyDriverWatcher.service.BackgroundService
import timber.log.Timber


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startService(Intent(this, BackgroundService::class.java))
    }
}