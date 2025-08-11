package com.carrental.speedmonitor.domain.notification

import javax.inject.Inject

class AwsSender @Inject constructor() : NotificationSender {
    override suspend fun sendAlert(customerId: String, speed: Float) {
        // Call AWS Lambda or SNS
    }
}
