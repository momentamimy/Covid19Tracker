package com.iti.intake40.covid_19tracker.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iti.intake40.covid_19tracker.data.COVIDRepo
import com.iti.intake40.covid_19tracker.data.COVIDRepository
import com.iti.intake40.covid_19tracker.data.model.COVID

class MainViewModel: ViewModel() {

    fun getLocalData(application: Application):LiveData<List<COVID>>?
    {
        val covidRepo: COVIDRepository = COVIDRepo(application)
        return  covidRepo.getCOVIDLocalData()
    }

    fun getData(application: Application):LiveData<List<COVID>>
    {
        val covidRepo: COVIDRepository = COVIDRepo(application)
        return  covidRepo.getCOVIDData()
    }
}