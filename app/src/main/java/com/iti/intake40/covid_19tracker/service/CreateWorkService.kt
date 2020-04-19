package com.iti.intake40.covid_19tracker.service

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class CreateWorkService (val context: Context,val repeatInterval:Long,val timeunit:TimeUnit){
    companion object
    fun startWork() :WorkRequest{
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequest.Builder(LoadFirstWorkerManger::class.java!!,repeatInterval,timeunit)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("CovidWork", ExistingPeriodicWorkPolicy.KEEP, request)

        return request
    }
    //update data by work manager
   /* private fun setupWorkManaager() {
        val work = createWorkRequest(Data.EMPTY)
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork("Smart work", ExistingPeriodicWorkPolicy.KEEP, work)

    }
    fun createWorkRequest(data: Data) =
        PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)

            .setConstraints(createConstraints())
            // setting a backoff on case the work needs to retry
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )

            .build()
}*/
}