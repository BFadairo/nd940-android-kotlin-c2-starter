package com.udacity.asteroidradar

import android.app.Application
import android.util.Log
import androidx.work.*
import com.udacity.asteroidradar.workers.FetchAsteroidsWorker
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.v("MainApplication", "Main Application")
        val fetchAsteroidWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<FetchAsteroidsWorker>(24, TimeUnit.HOURS)
                .setConstraints(Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresCharging(true)
                    .build()
                )
                .build()

        WorkManager
            .getInstance(applicationContext).enqueueUniquePeriodicWork("fetchAsteroids",
                ExistingPeriodicWorkPolicy.KEEP,
                fetchAsteroidWorkRequest)
    }
}