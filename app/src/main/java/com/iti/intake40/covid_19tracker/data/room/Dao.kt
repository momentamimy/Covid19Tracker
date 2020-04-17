package com.ITIKotlin.covid19trackerapp.model

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.iti.intake40.covid_19tracker.data.model.COVID

@Dao
interface Dao {
    @Insert
    suspend fun addCOVID(covid: COVID)

    @Update
    suspend fun updateCOVID(covid: COVID)

    @Query("delete from COVID")
    suspend fun deleteAllCOVID()

    @Query("select * from COVID where  country_name == :name")
    suspend fun getCOVID(name:String):COVID

    @Query("SELECT COUNT(*) from COVID")
    suspend fun getRowsCount() : Int

    @Query("select * from COVID")
    fun getAllCOVID(): LiveData<List<COVID>>
}