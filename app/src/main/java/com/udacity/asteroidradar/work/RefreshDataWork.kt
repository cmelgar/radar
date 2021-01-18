package com.udacity.asteroidradar.work

import android.content.Context
import android.content.res.Resources
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.AsteroidsListAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.getYesterday
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWork (appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    private val keyValue: String = applicationContext.resources.getString(R.string.nasa_api_key)

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }

    override suspend fun doWork(): Result {
        val database =  getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try {
            repository.refreshAsteroids(keyValue)
            repository.deletePastAsteroids(getYesterday())
            Result.success()
        }catch (exception: HttpException) {
            Result.retry()
        }
    }
}
