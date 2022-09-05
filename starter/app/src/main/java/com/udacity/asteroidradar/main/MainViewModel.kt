package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidRadarApi
import com.udacity.asteroidradar.api.AsteroidRadarApiService
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*

enum class AsteroidRadarApiStatus{LOADING, ERROR, DONE, NO_ITEMS}

class MainViewModel : ViewModel() {

    val lDates = getNextSevenDaysFormattedDates()
    val startDate = lDates[0]
    val endDate = lDates[7]

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
        getAsteroids()
        getApod()
    }

    private fun getAsteroids() {
        viewModelScope.launch{
            _status.value = AsteroidRadarApiStatus.LOADING
            try{
                val response: Response<String> = AsteroidRadarApi.retrofitService.getAsteroids(startDate = startDate, endDate = endDate, apiKey = Constants.Demo_KEY)
                if(response.isSuccessful){
                    val asteroidsList = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                    _asteroids.value = asteroidsList
                    Log.i("ViewModel","getAsteroids _asteroids.value from query =${_asteroids.value}")
                    if(asteroidsList.size > 0) {
                        _status.value = AsteroidRadarApiStatus.DONE
                    }else{
                        _status.value = AsteroidRadarApiStatus.NO_ITEMS
                    }
                }

            } catch (e: Exception){
                _status.value = AsteroidRadarApiStatus.ERROR
                Log.i("ViewModel","Error: ${e.message}")
            }
        }
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
}