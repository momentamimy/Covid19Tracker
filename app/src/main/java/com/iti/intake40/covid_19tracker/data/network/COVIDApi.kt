package com.iti.intake40.covid_19tracker.data.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

 interface COVIDApi {
    @Headers(
        "x-rapidapi-key: 7e10176121msh641642cfa83ca13p13b1d0jsnf085d23fc40f"
    )
    @GET("cases_by_country.php")
    fun getData(): Call<ResponseBody>
}