package com.carrental.speedmonitor.domain.notification

import com.carrental.speedmonitor.domain.model.CustomerSettings
import javax.inject.Inject

class SpeedNotifier @Inject constructor(
    private val firebaseSender: FirebaseSender,
    private val awsSender: AwsSender
) {
    suspend fun notify(customer: CustomerSettings, speed: Float) {
        when (customer.notificationChannel.uppercase()) {
            "FIREBASE" -> firebaseSender.sendAlert(customer.id, speed)
            "AWS" -> awsSender.sendAlert(customer.id, speed)
        }
    }
}
