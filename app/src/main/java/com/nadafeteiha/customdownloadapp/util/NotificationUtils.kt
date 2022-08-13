package com.nadafeteiha.customdownloadapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.nadafeteiha.customdownloadapp.DetailActivity
import com.nadafeteiha.customdownloadapp.R
import com.nadafeteiha.customdownloadapp.Status
import com.nadafeteiha.customdownloadapp.util.Constants.CHANNEL_NAME
import com.nadafeteiha.customdownloadapp.util.Constants.FILE_NAME
import com.nadafeteiha.customdownloadapp.util.Constants.STATUS

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0

fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            applicationContext.getString(R.string.notification_channel_id),
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = this.getColor(R.color.colorPrimary)
        notificationChannel.enableVibration(false)
        notificationChannel.description = getString(R.string.button_download_complete)

        val notificationManager = this.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.sendNotification(
    fileName: String,
    applicationContext: Context,
    status: Status
) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(FILE_NAME, fileName)
    contentIntent.putExtra(STATUS, status.toString())

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(applicationContext.getString(R.string.notification_description))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    notify(NOTIFICATION_ID, builder.build())
}


fun NotificationManager.cancelNotifications() {
    cancelAll()
}