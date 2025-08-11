package com.carrental.speedmonitor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carrental.speedmonitor.data.local.entity.CustomerEntity

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers WHERE customerId = :id")
    suspend fun getCustomerById(id: String): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)
}
