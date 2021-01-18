package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM DatabaseAsteroid  ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate=:today ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate BETWEEN :today AND :seventhDay ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(today: String, seventhDay: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM DatabaseAsteroid WHERE closeApproachDate=:yesterday")
    fun deletePreviousAsteroids(yesterday: String)
}

@Database(entities = [DatabaseAsteroid::class], version = 2)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            AsteroidDatabase::class.java,
            "asteroids")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
