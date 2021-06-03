package com.udacity.asteroidradar.main

import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.asteroid.AsteroidRepository
import com.udacity.asteroidradar.api.database.AsteroidDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainViewModel(private val dataSource: AsteroidDao) : ViewModel() {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
//        callAsteroidApi("2021-06-02", "2021-06-09", "rpkESuINzPffq1qbOY8P9AfrTLXnvA8PjlW5OnhL")
        getAsteroidsByWeek()
        getPictureOfDayFromNasa("rpkESuINzPffq1qbOY8P9AfrTLXnvA8PjlW5OnhL")
    }

    fun getAsteroidsByDate() {
        viewModelScope.launch {
            // Format Date
            val currentDateTime = Calendar.getInstance().time
            val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.US)
            val formattedDate = formatter.format(currentDateTime)

            val asteroidList = getAsteroidsFromDatabaseByDate(formattedDate)
            _asteroidList.value = asteroidList
        }
    }

    fun getAsteroidsByWeek() {
        viewModelScope.launch {
            // Format the Date into the same format as what the API dates for queries
            val currentDate = Calendar.getInstance()
            val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.US)
            val formattedStartDate = formatter.format(currentDate.time)

            currentDate.add(Calendar.DATE, 7) // Add 7 Days as we want to get asteroids in a week span

            val formattedEndDate = formatter.format(currentDate.time)
            val asteroidList = getAsteroidsFromDatabaseByWeek(formattedStartDate, formattedEndDate)
            _asteroidList.value = asteroidList
        }
    }

    private fun getPictureOfDayFromNasa(apiKey: String) {
        viewModelScope.launch {
            val pictureOfDay = getPictureOfTheDay(apiKey)
            _pictureOfDay.value = pictureOfDay
        }
    }
    private fun callAsteroidApi(startDate: String, endDate: String, apiKey: String) {
        viewModelScope.launch {
            val asteroidList = callAsteroidClient(startDate, endDate, apiKey)
            insertAsteroidsIntoDatabase(asteroidList)
        }
    }

    private suspend fun getPictureOfTheDay(apiKey: String): PictureOfDay {
        val pictureOfDay = AsteroidRepository().getPictureOfDay(apiKey)
        return pictureOfDay
    }

    private suspend fun callAsteroidClient(startDate: String, endDate: String, apiKey: String): List<Asteroid> {
        val asteroidList = AsteroidRepository().getAsteroidData(startDate, endDate, apiKey)
        return asteroidList
    }

    private suspend fun insertAsteroidsIntoDatabase(asteroidList: List<Asteroid>) {
        withContext(Dispatchers.IO) {
            dataSource.insertAllAsteroids(asteroidList)
        }
        _asteroidList.value = asteroidList
    }

    private suspend fun getAsteroidsFromDatabaseByDate(date: String): List<Asteroid> {
        var asteroidList = listOf<Asteroid>()
        withContext(Dispatchers.IO) {
            asteroidList = dataSource.getAsteroidsByDate(date)
        }
        return asteroidList
    }

    private suspend fun getAsteroidsFromDatabaseByWeek(startDate: String, endDate: String): List<Asteroid> {
        var asteroidList = listOf<Asteroid>()
        withContext(Dispatchers.IO) {
            asteroidList = dataSource.getAsteroidsByWeek(startDate, endDate)
        }
        return asteroidList
    }
}