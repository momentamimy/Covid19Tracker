package com.iti.intake40.covid_19tracker.data.room

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.ITIKotlin.covid19trackerapp.model.Dao
import com.ITIKotlin.covid19trackerapp.model.DatabaseCovid19
import com.iti.intake40.covid_19tracker.data.model.COVID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.math.log

class LocalDataSource {

    val covidDao: Dao
    val context: Context

    constructor(context: Context) {
        var db: DatabaseCovid19 = DatabaseCovid19.getInstance(context)
        covidDao = db.getDAO()
        this.context = context
    }

    suspend fun deleteAllData() {
        covidDao.deleteAllCOVID()
    }

    suspend fun addAllData(list: List<COVID>) {
        if (covidDao.getRowsCount() == 0)
            for (index in list.indices) {
                covidDao.addCOVID(list.get(index))
            }
        else
            for (index in list.indices) {
                covidDao.updateCOVID(list.get(index))
            }
    }

    fun getAllCovid(): LiveData<List<COVID>>? {
        var list: LiveData<List<COVID>>? = null
        list = covidDao.getAllCOVID()
        return list
    }

    suspend fun getCovidByName(name: String) = covidDao.getCOVID(name)
}