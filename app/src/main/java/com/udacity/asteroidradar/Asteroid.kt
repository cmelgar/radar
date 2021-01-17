package com.udacity.asteroidradar

import android.os.Parcelable
import com.udacity.asteroidradar.api.NetworkAsteroid
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable



//fun List<NetworkAsteroid>.asDomainModel(): List<Asteroid> {
//    return map {
//        Asteroid(
//                id = it.id,
//                codename = it.codename,
//                closeApproachDate = it.closeApproachDate,
//                absoluteMagnitude = it.absoluteMagnitude,
//                estimatedDiameter = it.estimatedDiameter,
//                relativeVelocity = it.relativeVelocity,
//                distanceFromEarth = it.distanceFromEarth,
//                isPotentiallyHazardous = it.isPotentiallyHazardous
//        )
//    }
//}