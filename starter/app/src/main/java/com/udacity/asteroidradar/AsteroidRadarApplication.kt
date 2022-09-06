package com.udacity.asteroidradar

import android.app.Application
import android.util.Log
import androidx.work.*
import com.udacity.asteroidradar.worker.RefreshDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication: Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
        Log.i("Appl","delayedInit called to schedule work")

    }
    private fun delayedInit(){
        applicationScope.launch {
            Log.i("Appl","Calling setupRecurringWork")
            setupRecurringWork()
        }
    }
    private fun setupRecurringWork(){
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val repeatRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshDataWork.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatRequest)
        Log.i("SURW","RecurringWorkSetup")
    }

}