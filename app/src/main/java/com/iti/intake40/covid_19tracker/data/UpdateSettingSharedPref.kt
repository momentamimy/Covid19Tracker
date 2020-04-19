package com.iti.intake40.covid_19tracker.data

import android.content.Context
import android.content.SharedPreferences

class UpdateSettingSharedPref(val context: Context) {

    val sharedPref = context.getSharedPreferences("Updates", Context.MODE_PRIVATE)


    fun savePeriod(number:Long,date:String) {
        sharedPref.edit().putLong("Number", number).commit()
        sharedPref.edit().putString("Date", date).commit()
    }

    fun getPeriod():Pair<Long?,String?> = Pair(sharedPref.getLong("Number", 1),sharedPref.getString("Date", "Hour"))

}