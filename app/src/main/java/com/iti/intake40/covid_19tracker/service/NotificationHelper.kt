package com.iti.intake40.covid_19tracker.service

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.iti.intake40.covid_19tracker.R
import com.iti.intake40.covid_19tracker.data.model.COVID
import com.iti.intake40.covid_19tracker.view.activities.MainActivity

class NotificationHelper(
    base: Context?,
    val covid:COVID?,
    val title: String,
    val body: String
) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null
    val pendingIntent:PendingIntent
    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel Name"
    }
    init {
        val notificationIntent:Intent = Intent(base, MainActivity::class.java)

        notificationIntent.putExtra("covid",covid)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        pendingIntent = PendingIntent.getActivity(base, 0, notificationIntent, FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            channelID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(
            applicationContext,
            channelID
        )
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setColor(resources.getColor(R.color.colorPrimary))
            .setSmallIcon(R.drawable.border).setAutoCancel(true)


}