package com.udacity.asteroidradar

import android.app.Application
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.udacity.asteroidradar.workers.FetchAsteroidsWorker

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.v("MainApplication", "Main Application")
        val fetchAsteroidWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<FetchAsteroidsWorker>()
                .build()

        WorkManager
            .getInstance()
            .enqueue(fetchAsteroidWorkRequest)

    }
}