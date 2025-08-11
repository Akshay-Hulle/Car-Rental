package com.carrental.speedmonitor.framework.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.carrental.speedmonitor.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarSpeedNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val channelId = "speed_alert_channel"
    private val notificationId = 1001

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Speed Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Alerts when speed limit is exceeded" }

            context
                .getSystemService(Context.NOTIFICATION_SERVICE)
                .let { it as NotificationManager }
                .createNotificationChannel(channel)
        }
    }

//    Sends a "speed limit exceeded" notification to the infotainment system.
    fun sendSpeedExceededNotification(customerName: String, currentSpeed: Float, maxLimit: Float) {
        val notification: Notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_speed_alert)
            .setContentTitle("Speed Limit Exceeded")
            .setContentText("$customerName is at %.1f km/h (limit %d km/h)".format(currentSpeed, maxLimit.toInt()))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setAutoCancel(true)
            .build()

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .notify(notificationId, notification)
    }
}
