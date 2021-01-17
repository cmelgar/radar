package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    init {
        getPictureOfDay()

        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
        }
    }

//    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids = asteroidsRepository.asteroids // LiveData<List<Asteroid>>
    val _apod = MutableLiveData<PictureOfDay>()
    val apod: LiveData<PictureOfDay>
        get() = _apod

    private fun getPictureOfDay() {
        viewModelScope.launch {

            asteroidsRepository.getApod().let {
                _apod.value = it
            }
        }
    }
//            get() = _asteroids

//    val apod = asteroidsRepository.apod

//    private fun getAsteroids() {
//        viewModelScope.launch {
//            try{
//
//                _asteroids.value = asteroidsRepository.refreshAsteroids()
//
//            }catch (exception: Exception){
//
//            }
//        }
//    }

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