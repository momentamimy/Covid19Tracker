package com.iti.intake40.covid_19tracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.iti.intake40.covid_19tracker.data.COVIDRepo
import com.iti.intake40.covid_19tracker.data.COVIDRepository
import com.iti.intake40.covid_19tracker.data.SubscribeSharedPref

class DetailsViewModel : ViewModel() {

    fun isSubscribed(context: Context,countryName: String) = SubscribeSharedPref(context).isCountrySubscribed(countryName)

    fun isAlreadySubscribed(context: Context) = SubscribeSharedPref(context).isCountryAlreadySubscribed()


    fun getSubscribeCountry(context: Context): String =
        SubscribeSharedPref(context).getSubscribedCountry()!!

    fun subscribe(context: Context, countryName: String) {
        SubscribeSharedPref(context).subscribeCountry(countryName)
    }

}