package com.udacity.asteroidradar.main

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
    private val _arResponse = MutableLiveData<String>()
    val arResponse: LiveData<String>
    get() = _arResponse

    init{
        getAsteroids()
    }

    private fun getAsteroids() {
        viewModelScope.launch{
            try{
                val response: Response<String> = AsteroidRadarApi.retrofitService.getAsteroids()
                if(response.isSuccessful){
                    val asteroidsList = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                    _arResponse.value = "Success: ${asteroidsList.size} Asteroids Received"
                }

            } catch (e: Exception){
                _arResponse.value = "Failure ${e.message}"
            }
        }
    }
}