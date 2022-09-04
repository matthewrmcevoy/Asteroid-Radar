package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidRadarApi
import com.udacity.asteroidradar.api.AsteroidRadarApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val PERS_API_KEY = "pgQ6VCkYjXbFAo1riLI53nNtMC8XnRyVgRxTTJ3F"
private const val TEST_API_KEY = "DEMO_KEY"
private const val TEST_SDATE = "2022-09-03"
private const val TEST_EDATE = "2022-09-04"

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
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
            try{
                val response: Response<String> = AsteroidRadarApi.retrofitService.getAsteroids(startDate = TEST_SDATE, endDate = TEST_EDATE, apiKey = TEST_API_KEY)
                if(response.isSuccessful){
                    val asteroidsList = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                    _asteroids.value = asteroidsList
                    Log.i("ViewModel","getAsteroids _asteroids.value from query =${_asteroids.value}")
                    _status.value = "Success: $asteroidsList Asteroids Received"
                }

            } catch (e: Exception){
                _status.value = "Failure ${e.message}"
            }
        }
    }
    private fun getApod(){
        viewModelScope.launch {
            try{
                val response = AsteroidRadarApi.retrofitService.getAPOD(apiKey = TEST_API_KEY)
                _aPOD.value = response
                Log.i("ViewModel","getAPOD returned ${_aPOD.value}")
            }catch(e: Exception){

            }
        }
    }
}