package com.carrental.speedmonitor.domain.notification

import javax.inject.Inject

class FirebaseSender @Inject constructor() : NotificationSender {
    override suspend fun sendAlert(customerId: String, speed: Float) {
        // Here you could send a push notification via FCM topic
    }
}
