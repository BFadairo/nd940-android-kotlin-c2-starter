package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList


    init {
        val listOfAsteroids = mutableListOf<Asteroid>()
        for (i in 0..10) {
            val asteroid = Asteroid(Random.nextLong(), "Kepler $i", "2015-09-08",20.36, 1.0, 19.7498082027, 5.0, true )
            listOfAsteroids.add(asteroid)
        }
        _asteroidList.value = listOfAsteroids
    }
}