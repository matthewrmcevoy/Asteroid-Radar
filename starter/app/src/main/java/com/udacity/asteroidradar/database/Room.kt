package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.main.startDate
import kotlinx.coroutines.internal.synchronized

@Dao
interface AsteroidDao{
    @Query("select * from DatabaseAsteroid WHERE closeApproachDate >= :startDate ORDER BY closeApproachDate asc")
    fun getAsteroids(startDate: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALL(vararg asteroids: DatabaseAsteroid)

    @Query("delete from DatabaseAsteroid WHERE closeApproachDate < :startDate")
    fun deleteOldAsteroids(startDate: String)

    @Query("select * from DatabaseAsteroid WHERE closeApproachDate is :startDate")
    fun getTodayAsteroids(startDate: String): LiveData<List<Asteroid>>
}
@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase(){
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {

        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).build()
        }
    return INSTANCE
}