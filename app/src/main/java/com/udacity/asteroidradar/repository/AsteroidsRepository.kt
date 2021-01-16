package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.NetworkAsteroidsContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
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
        val asteroidsList = emptyList<Asteroid>()
        withContext(Dispatchers.IO) {

            val result = Network.neows.getAsteroids("2021-01-16",
                "2021-01-23",
                "73Qk1y9WCJPdhfgXtR6CdamyvB0MnmFWpptvc9fh")
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(result)).toList()

            database.asteroidDao.insertAll(*NetworkAsteroidsContainer(asteroidsList).asDatabaseModel())
        }
    }

    suspend fun getApod() {

        withContext(Dispatchers.IO) {
            _apod.value = Network.neows.getApod("73Qk1y9WCJPdhfgXtR6CdamyvB0MnmFWpptvc9fh")
        }
    }
}