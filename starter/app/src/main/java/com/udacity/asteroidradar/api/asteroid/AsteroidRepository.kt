package com.udacity.asteroidradar.api.asteroid

import android.util.Log
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONException
import org.json.JSONObject

class AsteroidRepository {
    private var asteroidClient = AsteroidClient().asteroidData

    suspend fun getPictureOfDay(apiKey: String): PictureOfDay {
        val pictureOfDay = asteroidClient.getPictureOfTheDay(apiKey)

        return pictureOfDay
    }

    suspend fun getAsteroidData(
        startDate: String,
        endDate: String,
        apiKey: String
    ): List<Asteroid> {
        val asteroidData = asteroidClient.getAsteroids(startDate, endDate, apiKey)
        Log.v("AsteroidRepository", asteroidData)


        val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidData))

        return asteroidList
    }

    private fun parseAsteroidResponse(asteroidData: String): List<Asteroid> {
        val formattedDates = getNextSevenDaysFormattedDates()
        val json = JSONObject(asteroidData)
        val nearEarthJobs = json.getJSONObject("near_earth_objects")

        val asteroidList = mutableListOf<Asteroid>()

        for (date in formattedDates) {
            try {
                val asteroidDataByDate = nearEarthJobs.getJSONArray(date)
                for (i in 0 until asteroidDataByDate.length()) {
                    val asteroidJson = asteroidDataByDate.getJSONObject(i)

                    val id = asteroidJson.getLong("id")
                    val codeName = asteroidJson.getString("name")
                    val closeApproachRootJson =
                        asteroidJson.getJSONArray("close_approach_data") // Get the Root Json Array for the Close Approach
                    val closeApproachJsonObject =
                        closeApproachRootJson.getJSONObject(0) // Get the item in the first index for the close approach
                    val closeApproachDate =
                        closeApproachJsonObject.getString("close_approach_date") // Get the String with key "close_approach_date"
                    val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                    val estimatedDiameter =
                        asteroidJson.getJSONObject("estimated_diameter").getJSONObject("miles")
                            .getDouble("estimated_diameter_max")

                    val relativeVelocity =
                        closeApproachJsonObject.getJSONObject("relative_velocity")
                            .getDouble("miles_per_hour") // Get Relative Velocity in Miles Per hour
                    val distanceFromEarth =
                        closeApproachJsonObject.getJSONObject("miss_distance").getString("miles")
                            .toDouble()
                    val isPotentiallyHazardous =
                        asteroidJson.getBoolean("is_potentially_hazardous_asteroid")

                    val asteroid = Asteroid(
                        id,
                        codeName,
                        closeApproachDate,
                        absoluteMagnitude,
                        estimatedDiameter,
                        relativeVelocity,
                        distanceFromEarth,
                        isPotentiallyHazardous
                    )
                    asteroidList.add(asteroid)
                    Log.v("AsteroidRepository", "Asteroid $asteroid")
                }
            } catch (exception: JSONException) {
                Log.v("AsteroidRepository", "${exception.message}")
            }
        }
        return asteroidList
    }
}