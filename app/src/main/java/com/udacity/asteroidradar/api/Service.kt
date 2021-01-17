package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//73Qk1y9WCJPdhfgXtR6CdamyvB0MnmFWpptvc9fh
interface NeoWsService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date")start_date: String,
        @Query("end_date")end_date: String,
        @Query("api_key")api_key: String): String

    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key")api_key: String): Deferred<PictureOfDay>
}
//
//private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()

object Network {
    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    val neows = retrofit.create(NeoWsService::class.java)
}

