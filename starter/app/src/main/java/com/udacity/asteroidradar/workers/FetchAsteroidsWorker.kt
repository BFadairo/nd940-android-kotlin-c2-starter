package com.udacity.asteroidradar.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.asteroid.AsteroidClient
import com.udacity.asteroidradar.api.asteroid.AsteroidRepository
import com.udacity.asteroidradar.api.database.AsteroidDao
import com.udacity.asteroidradar.api.database.AsteroidDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class FetchAsteroidsWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val database = AsteroidClient().asteroidData

        Log.v("WorkManager", "Inside Work manager")
        try {
            Log.v("WorkManager", "Inside try work manager")
            val database = AsteroidDatabase.getInstance(appContext).asteroidDao
            CoroutineScope(Dispatchers.IO).launch {
                fetchAsteroidsFromNasa(database)
            }
            Log.v("WorkManager", "Returning Success")
            return Result.success()
        } catch (throwable: Throwable) {
            Log.e("FetchAsteroidsWorker", "Error Fetching Asteroid List: ${throwable.message}")
            return Result.failure()
        }
    }

    private suspend fun fetchAsteroidsFromNasa(database: AsteroidDao) {
        // Format the Date into the same format as what the API dates for queries
        val currentDate = Calendar.getInstance()
        val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.US)
        val formattedStartDate = formatter.format(currentDate.time)

        currentDate.add(Calendar.DATE, 7) // Add 7 Days as we want to get asteroids in a week span

        val formattedEndDate = formatter.format(currentDate.time)

        val asteroidList = AsteroidRepository().getAsteroidData(formattedStartDate, formattedEndDate, "rpkESuINzPffq1qbOY8P9AfrTLXnvA8PjlW5OnhL")

        database.deleteAllAsteroidsBeforeToday(formattedStartDate) // Delete all Asteroids before the current date out of the database

        database.insertAllAsteroids(asteroidList) // Insert new asteroid data into the database
    }
}