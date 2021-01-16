package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val asteroids: List<NetworkAsteroids>)

@JsonClass(generateAdapter = true)
data class NetworkAsteroids(
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absolute_magnitude: Double,
        val estimated_diameter_max: Double,
        val relative_velocity: Double,
        val distance_from_earth: Double,
        val is_potentially_hazardous_asteroid: Boolean
)

fun NetworkAsteroidsContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid (
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absolute_magnitude = it.absolute_magnitude,
                estimated_diameter_max = it.estimated_diameter_max,
                relative_velocity = it.relative_velocity,
                distance_from_earth = it.distance_from_earth,
                is_potentially_hazardous_asteroid = it.is_potentially_hazardous_asteroid
        )
    }.toTypedArray()
}