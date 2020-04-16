package com.iti.intake40.covid_19tracker.viewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.iti.intake40.covid_19tracker.data.COVIDRepo
import com.iti.intake40.covid_19tracker.data.COVIDRepository
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.service.CreateWorkService
import com.iti.intake40.covid_19tracker.view.activities.MainActivity

class MainViewModel : ViewModel() {

    fun getLocalData(application: Application): LiveData<List<COVID>>? {
        val covidRepo: COVIDRepository = COVIDRepo(application)
        return covidRepo.getCOVIDLocalData()
    }

    fun getData(application: Application): LiveData<List<COVID>> {
        val covidRepo: COVIDRepository = COVIDRepo(application)
        return covidRepo.getCOVIDData()
    }

    fun createPeriodicWork(application: Application) :WorkRequest = CreateWorkService(application).startWork()

}