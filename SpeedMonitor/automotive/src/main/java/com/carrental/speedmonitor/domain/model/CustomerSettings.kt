package com.carrental.speedmonitor.domain.model

data class CustomerSettings(
    val id: String,
    val name: String,
    val maxSpeedLimit: Float,
    val notificationChannel : String
)