package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val near_earth_objects: List<Asteroid>)

fun NetworkAsteroidsContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return near_earth_objects.map {
        DatabaseAsteroid (
                id = it.id,
                codename = "test",
                closeApproachDate = "test",
                absolute_magnitude = 0.5,
                estimated_diameter_max = 0.9,
                relative_velocity = 0.8,
                distance_from_earth = 0.1,
                is_potentially_hazardous_asteroid = true
        )
    }.toTypedArray()
}

fun NetworkAsteroidsContainer.asDomainModel(): List<Asteroid> {
    return near_earth_objects.map {
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