package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    var _apod = MutableLiveData<PictureOfDay>()
    val apod: LiveData<PictureOfDay>
        get() = _apod

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {

            val result = Network.neows.getAsteroids("2021-01-17",
                "2021-01-24",
                "73Qk1y9WCJPdhfgXtR6CdamyvB0MnmFWpptvc9fh")

            val jsonAsteroid = JSONObject(result)
            val asteroidsList = parseAsteroidsJsonResult(jsonAsteroid).toList()

//
//            System.out.println(asteroidsList)
//
            database.asteroidDao.insertAll(*NetworkAsteroidsContainer(asteroidsList).asDatabaseModel())
            System.out.println("adios")
            database.asteroidDao.getAsteroids().value?.let {
                System.out.println("adios2")
                System.out.println(it.size)
            }
        }
    }

    suspend fun getApod() {

        withContext(Dispatchers.IO) {
            _apod.value = Network.neows.getApod("73Qk1y9WCJPdhfgXtR6CdamyvB0MnmFWpptvc9fh").await()
        }
    }
}