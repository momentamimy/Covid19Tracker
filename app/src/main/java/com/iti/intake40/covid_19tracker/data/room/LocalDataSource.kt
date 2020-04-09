package com.iti.intake40.covid_19tracker.data.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.ITIKotlin.covid19trackerapp.model.Dao
import com.ITIKotlin.covid19trackerapp.model.DatabaseCovid19
import com.iti.intake40.covid_19tracker.data.model.COVID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LocalDataSource {

    val covidDao: Dao
    val application: Application

    constructor(application: Application) {
        var db: DatabaseCovid19 = DatabaseCovid19.getInstance(application)
        covidDao = db.getDAO()
        this.application = application
    }

    fun deleteAllData() {
        CoroutineScope(IO).launch{
            covidDao.deleteAllCOVID()
        }
    }

    fun addAllData(list: List<COVID>) {
        CoroutineScope(IO).launch {
            for (index in 0..list.size - 1) {
                covidDao.addCOVID(list.get(index))
            }
        }
    }

    fun getAllCovid(): LiveData<List<COVID>>? {
        var list:LiveData<List<COVID>>?=null
            list=covidDao.getAllCOVID()
        return list
    }
}