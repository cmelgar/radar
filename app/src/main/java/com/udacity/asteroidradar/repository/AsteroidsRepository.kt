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

class AsteroidsRepository (private val database: AsteroidDatabase) {
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getTodayAsteroids()) {
        it.asDomainModel()
    }

    val asteroidsSaved: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    val asteroidsWeek: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getWeekAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(app_key: String) {
        withContext(Dispatchers.IO) {
            val result = Network.neows.getAsteroids("2021-01-17",
                "2021-01-24",
                app_key)
            val jsonAsteroid = JSONObject(result)
            val asteroidsList = parseAsteroidsJsonResult(jsonAsteroid).toList()

            database.asteroidDao.insertAll(*NetworkAsteroidsContainer(asteroidsList).asDatabaseModel())
        }
    }

    suspend fun getApod(app_key: String): PictureOfDay {
        return Network.neows.getApod(app_key)
    }
}