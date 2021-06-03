package com.udacity.asteroidradar.api.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(asteroids: List<Asteroid>)

    @Query ("SELECT * from asteroid_list")
    fun getAllAsteroids() : List<Asteroid>

    @Query("SELECT * FROM asteroid_list WHERE close_approach_date = :date")
    fun getAsteroidsByDate(date: String) : List<Asteroid>

    @Query("SELECT * FROM asteroid_list WHERE close_approach_date BETWEEN :startDate AND :endDate ORDER BY close_approach_date ASC")
    fun getAsteroidsByWeek(startDate: String, endDate: String) : List<Asteroid>

    @Query("DELETE FROM asteroid_list WHERE close_approach_date < :todayDate")
    fun deleteAllAsteroidsBeforeToday(todayDate: String)
}