package com.iti.intake40.covid_19tracker.data

import androidx.lifecycle.LiveData
import com.iti.intake40.covid_19tracker.data.model.COVID

interface COVIDRepository {

     fun getCOVIDLocalData():LiveData<List<COVID>>?
     fun getCOVIDData():LiveData<List<COVID>>
     fun getCOVIDDataFromWork(): LiveData<List<COVID>>
}