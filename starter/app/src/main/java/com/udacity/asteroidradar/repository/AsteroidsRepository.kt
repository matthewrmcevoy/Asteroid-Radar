package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidRadarApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.main.AsteroidRadarApiStatus
import com.udacity.asteroidradar.main.endDate
import com.udacity.asteroidradar.main.startDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {
    var asteroidsList = ArrayList<Asteroid>()
    val asteroidsDaily = database.asteroidDao.getTodayAsteroids(startDate)
    var asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids(startDate)){
        it.asDomainModel()
    }
    val status = MutableLiveData<AsteroidRadarApiStatus>()


    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
        status.value = AsteroidRadarApiStatus.LOADING
            try {
                val response = AsteroidRadarApi.retrofitService.getAsteroids(
                    startDate,
                    endDate,
                    Constants.API_KEY
                )
                asteroidsList = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                database.asteroidDao.insertALL(*asteroidsList.asDatabaseModel())
                status.value = AsteroidRadarApiStatus.DONE
            }catch(e: Exception){
                Log.i("Repository","No Internet Connection")
                status.value = AsteroidRadarApiStatus.ERROR
            }
        }
    }
    suspend fun getDailyAsteroids(){
        withContext(Dispatchers.IO){
            try{
                asteroids = database.asteroidDao.getTodayAsteroids(startDate)
                Log.i("Repository","changed asteroidData to ${asteroids.value}")
            }catch(e: Exception){
                Log.i("Repository", "failure: ${e.message}")
            }
        }
    }
}