package com.iti.intake40.covid_19tracker.data

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.data.network.RetrofitClient
import com.iti.intake40.covid_19tracker.data.room.LocalDataSource
import com.iti.intake40.covid_19tracker.service.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.full.memberProperties

class COVIDRepo(val context: Context) : COVIDRepository {
    val localDataSource: LocalDataSource

    init {
        localDataSource = LocalDataSource(context)
    }

    //LocaleData
    override fun getCOVIDLocalData(): LiveData<List<COVID>>? {
        return localDataSource.getAllCovid()
    }


    //RemoteData
    val gson = GsonBuilder()
        .setLenient()
        .create()

    override fun getCOVIDData(): LiveData<List<COVID>> {
        val data = MutableLiveData<List<COVID>>()
        val call: Call<ResponseBody> = RetrofitClient.getClient.getData()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                val obj = JSONObject(response!!.body()!!.string())
                val arr: JSONArray = obj.getJSONArray("countries_stat")
                data.value = gson.fromJson(arr.toString(), Array<COVID>::class.java).toList()

                CoroutineScope(IO).launch {
                    insertAllCovidIntoRoom(data.value!!)
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                data.value = null
            }
        })
        return data
    }


    override fun getCOVIDDataFromWork(): LiveData<List<COVID>> {
        val data = MutableLiveData<List<COVID>>()
        val call: Call<ResponseBody> = RetrofitClient.getClient.getData()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val obj = JSONObject(response!!.body()!!.string())
                val arr: JSONArray = obj.getJSONArray("countries_stat")
                data.value = gson.fromJson(arr.toString(), Array<COVID>::class.java).toList()

                CoroutineScope(IO).launch {
                    checkSubscribedCountry(data.value!!)
                    insertAllCovidIntoRoom(data.value!!)
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                data.value = null
            }
        })
        return data
    }

    private suspend fun insertAllCovidIntoRoom(list: List<COVID>) {
        localDataSource.addAllData(list)
    }

    private suspend fun checkSubscribedCountry(list: List<COVID>) {
        val subscribeSharedPref = SubscribeSharedPref(context)
        if (subscribeSharedPref.isCountryAlreadySubscribed()) {

            val covid: COVID =
                localDataSource.getCovidByName(subscribeSharedPref.getSubscribedCountry()!!)
            var newCOVID: COVID = covid
            list.forEach {
                if (it.countryName.equals(subscribeSharedPref.getSubscribedCountry()))
                    newCOVID = it
            }
            for (prop in COVID::class.memberProperties) {
                if (!prop.get(newCOVID)!!.equals(prop.get(covid))) {
                    print("LAAAAAAAAAA Changes")
                    triggerNotification(newCOVID)
                    break
                }
            }
        }
    }

    private fun triggerNotification(covid: COVID) {
        val notificationHelper =
            NotificationHelper(context, covid,covid.countryName, "There are changes in the country")
        val nb: NotificationCompat.Builder = notificationHelper.channelNotification
        notificationHelper.manager?.notify(1, nb.build())
    }
}