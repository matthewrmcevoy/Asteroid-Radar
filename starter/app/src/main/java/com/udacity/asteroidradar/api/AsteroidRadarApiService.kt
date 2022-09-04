package com.udacity.asteroidradar.api

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/neo/rest/v1/"
private const val PERS_API_KEY = "pgQ6VCkYjXbFAo1riLI53nNtMC8XnRyVgRxTTJ3F"
private const val TEST_API_KEY = "DEMO_KEY"
private const val TEST_SDATE = "2022-09-03"
private const val TEST_EDATE = "2022-09-04"

private val moshi = Moshi.Builder()
    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidRadarApiService {
    @GET("feed?start_date=2022-09-03&end_date=2022-09-04&api_key=DEMO_KEY")
    //fun getProperties(@Query("feed") type: String): Call<String>
    suspend fun getAsteroids(): Response<String>
}
object AsteroidRadarApi {
    val retrofitService : AsteroidRadarApiService by lazy{
        retrofit.create(AsteroidRadarApiService::class.java)
    }
}