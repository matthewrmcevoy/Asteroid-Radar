package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidRadarApi
import com.udacity.asteroidradar.api.AsteroidRadarApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
    get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
    get()= _asteroids

    init{
        getAsteroids()
    }

    private fun getAsteroids() {
        viewModelScope.launch{
            try{
                val response: Response<String> = AsteroidRadarApi.retrofitService.getAsteroids()
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
}