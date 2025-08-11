package com.carrental.speedmonitor.domain.notification

interface NotificationSender {
    suspend fun sendAlert(customerId: String, speed: Float)
}
