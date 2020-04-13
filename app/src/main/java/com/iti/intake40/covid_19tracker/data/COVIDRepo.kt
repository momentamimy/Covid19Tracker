package com.iti.intake40.covid_19tracker.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.data.network.RetrofitClient
import com.iti.intake40.covid_19tracker.data.room.LocalDataSource
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class COVIDRepo(application: Application) : COVIDRepository {
    val localDataSource:LocalDataSource

    init {
        localDataSource = LocalDataSource(application)
    }
    override fun getCOVIDLocalData(): LiveData<List<COVID>>? {
        return localDataSource.getAllCovid()
    }

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
                localDataSource.deleteAllData()
                localDataSource.addAllData(data.value!!)
            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                data.value = null
            }
        })

         return  data
    }
}