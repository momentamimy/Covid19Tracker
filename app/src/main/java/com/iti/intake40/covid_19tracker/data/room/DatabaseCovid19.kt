package com.ITIKotlin.covid19trackerapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iti.intake40.covid_19tracker.data.model.COVID

@Database(entities = arrayOf(COVID::class),version = 1)
abstract class DatabaseCovid19 : RoomDatabase() {
    abstract fun getDAO():Dao

    companion object
    {
        private var INSTANCE:DatabaseCovid19? = null

        fun getInstance(context:Context):DatabaseCovid19 {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseCovid19::class.java,
                    "Covid_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }



}