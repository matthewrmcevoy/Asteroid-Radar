package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Dao
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidRadarApi
import com.udacity.asteroidradar.api.AsteroidRadarApiService
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*

enum class AsteroidRadarApiStatus{LOADING, ERROR, DONE, NO_ITEMS}

val lDates = getNextSevenDaysFormattedDates()
val startDate = lDates[0]
val endDate = lDates[7]

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidsRepository(database)

    private val _status = MutableLiveData<AsteroidRadarApiStatus>()
    val status: LiveData<AsteroidRadarApiStatus>
    get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
    get()= _asteroids

    private val _aPOD = MutableLiveData<PictureOfDay>()
    val aPOD: LiveData<PictureOfDay>
    get()= _aPOD

    init{
        //getAsteroids()
       getApod()
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()

        }

    }
    val test = asteroidRepository.asteroids

//    private fun getAsteroids() {
//        viewModelScope.launch{
//            _status.value = AsteroidRadarApiStatus.LOADING
//            try{
//                val response = AsteroidRadarApi.retrofitService.getAsteroids(startDate, endDate, Constants.Demo_KEY)
//                if(response.isSuccessful){
//                    val asteroidsList = parseAsteroidsJsonResult(JSONObject(response.body()!!))
//                    _asteroids.value = asteroidsList
//                    Log.i("ViewModel","getAsteroids _asteroids.value from query =${_asteroids.value}")
//                    if(asteroidsList.size > 0) {
//                        _status.value = AsteroidRadarApiStatus.DONE
//                    }else{
//                        _status.value = AsteroidRadarApiStatus.NO_ITEMS
//                    }
//                }
//
//            } catch (e: Exception){
//                _status.value = AsteroidRadarApiStatus.ERROR
//                Log.i("ViewModel","Error: ${e.message}")
//            }
//        }
//    }
    private fun getApod(){
        viewModelScope.launch {
            try{
                val response = AsteroidRadarApi.retrofitService.getAPOD(apiKey = Constants.API_KEY)
                _aPOD.value = response
                Log.i("ViewModel","getAPOD returned ${_aPOD.value}")
            }catch(e: Exception){
            }
        }
    }
}