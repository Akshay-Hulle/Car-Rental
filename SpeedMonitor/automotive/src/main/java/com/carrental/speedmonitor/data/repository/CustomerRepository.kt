package com.carrental.speedmonitor.data.repository

import com.carrental.speedmonitor.data.local.dao.CustomerDao
import com.carrental.speedmonitor.domain.model.CustomerSettings
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) {
    suspend fun getCustomerSettings(id: String): CustomerSettings {
        val entity = customerDao.getCustomerById(id)
            ?: throw IllegalArgumentException("Customer not found")

        return CustomerSettings(
            id = entity.customerId,
            name = entity.customerName,
            maxSpeedLimit = entity.maxSpeedLimit,
            notificationChannel = entity.notificationChannel
        )
    }

    suspend fun saveCustomer(settings: CustomerSettings) {
        customerDao.insertCustomer(
            com.carrental.speedmonitor.data.local.entity.CustomerEntity(
                customerId = settings.id,
                customerName = settings.name,
                maxSpeedLimit = settings.maxSpeedLimit,
                notificationChannel = settings.notificationChannel
            )
        )
    }
}
