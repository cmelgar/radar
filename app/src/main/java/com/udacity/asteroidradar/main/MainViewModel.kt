package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

enum class NasaApiStatus { LOADING, ERROR, DONE }
enum class MediaType(val value: String) { IMAGE("image"), VIDEO("video") }

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val keyValue: String = application.resources.getString(R.string.nasa_api_key)

    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _onOptionChanged = MutableLiveData<Int>()
    val onOptionChanged: LiveData<Int>
        get() = _onOptionChanged

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    lateinit var asteroids: LiveData<List<Asteroid>>

    init {
        _status.value = NasaApiStatus.LOADING
        getPictureOfDay()
        viewModelScope.launch {
            onQueryChanged()
            asteroidsRepository.refreshAsteroids(keyValue)
            _status.value = NasaApiStatus.DONE
        }
    }


    val _apod = MutableLiveData<PictureOfDay>()
    val apod: LiveData<PictureOfDay>
        get() = _apod

    private fun onQueryChanged() {
        try {
            asteroids = asteroidsRepository.asteroids
        }catch (e: IOException) {

        }
    }

    fun showOptionSelected(optionSelected: OptionSelected) {

        asteroids = when (optionSelected) {
            OptionSelected.SAVED -> {
                asteroidsRepository.asteroidsSaved
            }
            OptionSelected.WEEK -> {
                //asteroidsRepository.asteroidsWeek
                Transformations.map(database.asteroidDao.getWeekAsteroids(
                    getToday(), getSeventhDay()
                )) {
                    it.asDomainModel()
                }
            }
            else -> {
                asteroidsRepository.asteroids
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try{
                asteroidsRepository.getApod(keyValue).let {
                    if(it.mediaType == MediaType.IMAGE.value) {
                            _apod.value = it
                        }
                }
            }catch (e: IOException){
                _apod.value = null
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