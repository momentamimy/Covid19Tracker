package com.iti.intake40.covid_19tracker.service

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class CreateWorkService (val context: Context){
    companion object
    fun startWork() :WorkRequest{
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequest.Builder(LoadFirstWorkerManger::class.java!!,20,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(request)

        return request
    }
}