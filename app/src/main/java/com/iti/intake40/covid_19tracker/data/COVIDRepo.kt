package com.iti.intake40.covid_19tracker.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.data.network.RetrofitClient
import com.iti.intake40.covid_19tracker.data.room.LocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class COVIDRepo(val context: Context) : COVIDRepository {
    val localDataSource:LocalDataSource

    init {
        localDataSource = LocalDataSource(context)
    }
    //LocaleData
    override fun getCOVIDLocalData(): LiveData<List<COVID>>? {
        return localDataSource.getAllCovid()
    }
    override fun insertAllCovidIntoRoom(list:List<COVID>)
    {
        localDataSource.addAllData(list)
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

                insertAllCovidIntoRoom(data.value!!)

            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                data.value = null
            }
        })
         return  data
    }


    override fun getCOVIDDataFromWork(): LiveData<List<COVID>> {
        val data = MutableLiveData<List<COVID>>()
        val call: Call<ResponseBody> = RetrofitClient.getClient.getData()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val obj = JSONObject(response!!.body()!!.string())
                val arr: JSONArray = obj.getJSONArray("countries_stat")
                data.value = gson.fromJson(arr.toString(), Array<COVID>::class.java).toList()

                checkSubscribedCountry()
                insertAllCovidIntoRoom(data.value!!)
            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                data.value = null
            }
        })
        return  data
    }

    private fun checkSubscribedCountry() {
        val subscribeSharedPref:SubscribeSharedPref= SubscribeSharedPref(context)
        subscribeSharedPref.getSubscribedCountry()
    }
}