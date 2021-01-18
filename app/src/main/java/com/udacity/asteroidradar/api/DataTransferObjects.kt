package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val asteroids: List<Asteroid>)

//@JsonClass(generateAdapter = true)
//data class NetworkAsteroid(
//        val id: Long,
//        val codename: String,
//        val closeApproachDate: String,
//        val absoluteMagnitude: Double,
//        val estimatedDiameter: Double,
//        val relativeVelocity: Double,
//        val distanceFromEarth: Double,
//        val isPotentiallyHazardous: Boolean
//)

fun NetworkAsteroidsContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid (
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absolute_magnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

fun NetworkAsteroidsContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}