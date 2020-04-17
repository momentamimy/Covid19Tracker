package com.iti.intake40.covid_19tracker.service

import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.iti.intake40.covid_19tracker.data.COVIDRepo
import com.iti.intake40.covid_19tracker.data.COVIDRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class LoadFirstWorkerManger(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result
    {
        loadData()
        return Result.success()
    }

    private fun loadData() {
        val covidRepo: COVIDRepository = COVIDRepo(context)
        covidRepo.getCOVIDDataFromWork()
    }
}