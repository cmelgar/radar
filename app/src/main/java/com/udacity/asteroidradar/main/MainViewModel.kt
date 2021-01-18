package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val keyValue: String = application.resources.getString(R.string.nasa_api_key)

    private val _filter = MutableLiveData<Int>()
    val filter: LiveData<Int>
        get() = _filter

    init {
        getPictureOfDay()

        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids(keyValue)
        }
    }

    var asteroids = asteroidsRepository.asteroids
    val _apod = MutableLiveData<PictureOfDay>()
    val apod: LiveData<PictureOfDay>
        get() = _apod

    private fun filterApplied() {
        asteroids = when (filter.value) {
            1 -> asteroidsRepository.asteroidsSaved
            2 -> asteroidsRepository.asteroidsWeek
            else -> asteroidsRepository.asteroids
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {

            asteroidsRepository.getApod(keyValue).let {
                _apod.value = it
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}