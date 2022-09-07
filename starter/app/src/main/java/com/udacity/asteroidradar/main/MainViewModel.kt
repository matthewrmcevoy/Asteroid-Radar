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
import com.udacity.asteroidradar.database.asDomainModel
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

    //var asteroidsView = asteroidRepository.asteroids
    lateinit var asteroidsView:LiveData<List<Asteroid>>

    private val _aPOD = MutableLiveData<PictureOfDay>()
    val aPOD: LiveData<PictureOfDay>
    get()= _aPOD

    private val _showAsteroidDetail=MutableLiveData<Asteroid>()
    val showAsteroidDetail: LiveData<Asteroid>
    get()= _showAsteroidDetail

    init{
        getApod()
        asteroidsView = asteroidRepository.asteroids
    }

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
    fun displayAsteroidDetails(asteroid: Asteroid){
        _showAsteroidDetail.value = asteroid
    }
    fun displayAsteroidDetailComplete(){
        _showAsteroidDetail.value = null
    }
    fun displayWeekAsteroids(){
        asteroidsView = asteroidRepository.asteroids
        }

    fun displayDailyAsteroids(){
        Log.i("ViewModel","current asteroids: ${asteroidsView.value}")
        asteroidsView = asteroidRepository.asteroidsDaily
        Log.i("ViewModel","new asteroids: ${asteroidsView.value}")
    }
}