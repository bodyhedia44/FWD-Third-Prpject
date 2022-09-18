package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf


private val NOTIFICATION_ID=0
private val REQUEST_CODE=0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context,Name: String,statue: String) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val snoozeIntent = Intent(applicationContext, DetailActivity::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(
        applicationContext,
        "Download"
    ).setSmallIcon(R.drawable.done)
        .setContentTitle(applicationContext
         .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.done,
            applicationContext.getString(R.string.notification_button),
            snoozePendingIntent
        )

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}