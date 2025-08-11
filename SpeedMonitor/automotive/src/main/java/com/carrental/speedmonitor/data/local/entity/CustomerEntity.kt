package com.carrental.speedmonitor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val customerId: String,
    val customerName: String,
    val maxSpeedLimit: Float,
    val notificationChannel : String
)
