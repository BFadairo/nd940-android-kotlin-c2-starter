package com.udacity.asteroidradar.api.asteroid

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidData {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("start_date") startDate: String, @Query("end_date") endDate: String, @Query("api_key") apiKey: String ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String) : PictureOfDay
}