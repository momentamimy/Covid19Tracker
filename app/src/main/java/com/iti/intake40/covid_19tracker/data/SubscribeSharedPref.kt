package com.iti.intake40.covid_19tracker.data

import android.content.Context
import android.content.SharedPreferences

class SubscribeSharedPref(val context: Context) {

    val sharedPref = context.getSharedPreferences("Country", Context.MODE_PRIVATE)

    fun isCountrySubscribed(countryName: String) = (getSubscribedCountry().equals(countryName))

    fun isCountryAlreadySubscribed() = !(getSubscribedCountry().equals(""))

    fun subscribeCountry(countryName: String) = sharedPref.edit().putString("Subscribe_Country", countryName).commit()

    fun getSubscribedCountry() = sharedPref.getString("Subscribe_Country", "")

}