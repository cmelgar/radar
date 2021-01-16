package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absolute_magnitude: Double,
    val estimated_diameter_max: Double,
    val relative_velocity: Double,
    val distance_from_earth: Double,
    val is_potentially_hazardous_asteroid: Boolean
//    val kilometers_per_second: String,
//    val astronomical: String
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absolute_magnitude,
                estimatedDiameter = it.estimated_diameter_max,
                relativeVelocity = it.relative_velocity,
                distanceFromEarth = it.distance_from_earth,
                isPotentiallyHazardous = it.is_potentially_hazardous_asteroid
        )
    }
}