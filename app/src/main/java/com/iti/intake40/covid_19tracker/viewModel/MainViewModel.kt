package com.iti.intake40.covid_19tracker.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iti.intake40.covid_19tracker.data.COVIDRepo
import com.iti.intake40.covid_19tracker.data.COVIDRepository
import com.iti.intake40.covid_19tracker.data.model.COVID

class MainViewModel: ViewModel() {
    private val covidRepo: COVIDRepository = COVIDRepo()
     fun getData():LiveData<List<COVID>>
    {
        return   covidRepo.getCOVIDData()
    }
}