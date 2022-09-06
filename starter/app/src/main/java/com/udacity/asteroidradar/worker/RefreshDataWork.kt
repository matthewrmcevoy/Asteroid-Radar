package com.udacity.asteroidradar.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.main.startDate
import com.udacity.asteroidradar.repository.AsteroidsRepository

class RefreshDataWork(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try{
            repository.refreshAsteroids()
            Log.i("Worker","refreshAsteroids called from background to insert new data")
            database.asteroidDao.deleteOldAsteroids(startDate)
            Log.i("Worker","deleteOldAsteroids called to remove data older than $startDate")
            Result.success()
        }catch(e: Exception){
            Log.i("Worker","Exception during worker process ${e.message}")
            Result.retry()
        }

    }
    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }
}