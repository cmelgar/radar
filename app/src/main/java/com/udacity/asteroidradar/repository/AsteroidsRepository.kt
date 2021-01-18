package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class AsteroidsRepository (private val database: AsteroidDatabase) {
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getTodayAsteroids(
        getToday())) {
        it.asDomainModel()
    }

    val asteroidsSaved: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    val asteroidsWeek: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getWeekAsteroids(
        getToday(), getSeventhDay()
    )) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(app_key: String) {
        withContext(Dispatchers.IO) {
            try {
                val result = Network.neows.getAsteroids(
                    getToday(),
                    getSeventhDay(),
                    app_key)
                val jsonAsteroid = JSONObject(result)
                val asteroidsList = parseAsteroidsJsonResult(jsonAsteroid).toList()

                database.asteroidDao.insertAll(*NetworkAsteroidsContainer(asteroidsList).asDatabaseModel())
            }catch (e: Exception) {

            }
        }
    }

    suspend fun getApod(app_key: String): PictureOfDay {
           return Network.neows.getApod(app_key)
    }

    suspend fun deletePastAsteroids(yesterday: String) {
        database.asteroidDao.deletePreviousAsteroids(yesterday)
    }
}