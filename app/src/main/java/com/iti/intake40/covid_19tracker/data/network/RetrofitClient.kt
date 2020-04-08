package com.iti.intake40.covid_19tracker.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {

    private const val Url:String="https://coronavirus-monitor.p.rapidapi.com/coronavirus/"
    val getClient: COVIDApi
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(Url)
                .client(client)
                .build()
            return retrofit.create(COVIDApi::class.java)
        }
}