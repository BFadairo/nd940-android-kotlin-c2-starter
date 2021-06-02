package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.asteroid.AsteroidRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _flattenedAsteroidList = MutableLiveData<List<Asteroid>>()
    val flattenedAsteroidList: LiveData<List<Asteroid>>
        get() = _flattenedAsteroidList


    init {
        callAsteroidApi("2021-06-02", "2021-06-09", "rpkESuINzPffq1qbOY8P9AfrTLXnvA8PjlW5OnhL")
    }

    fun callAsteroidApi(startDate: String, endDate: String, apiKey: String) {
        viewModelScope.launch {
            callAsteroidClient(startDate, endDate, apiKey)
        }
    }

    private suspend fun callAsteroidClient(startDate: String, endDate: String, apiKey: String) {
        val asteroidList = AsteroidRepository().getAsteroidData(startDate, endDate, apiKey)
        _asteroidList.value = asteroidList
    }
}