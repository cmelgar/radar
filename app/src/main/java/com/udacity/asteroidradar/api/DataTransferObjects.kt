package com.udacity.asteroidradar.api

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AsteroidDate(
    @Json(name = "id") val id: String
): Parcelable

@Parcelize
data class NearEarthObjects(
    @Json(name = "2015-09-08") val date: List<AsteroidDate>
): Parcelable

@Parcelize
data class NeoWsResponse(
    @Json(name = "near_earth_objects") val near_earth_objects: NearEarthObjects
): Parcelable

@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val asteroids: List<NeoWsResponse>)

//@JsonClass(generateAdapter = true)
//data class NetworkAsteroids(
//        val id: Long,
//        val codename: String,
//        val closeApproachDate: String,
//        val absolute_magnitude: Double,
//        val estimated_diameter_max: Double,
//        val relative_velocity: Double,
//        val distance_from_earth: Double,
//        val is_potentially_hazardous_asteroid: Boolean
//)

fun NetworkAsteroidsContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid (
                id = it.near_earth_objects.date[0].id.toLong(),
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