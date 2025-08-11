package com.carrental.speedmonitor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carrental.speedmonitor.data.local.dao.CustomerDao
import com.carrental.speedmonitor.data.local.entity.CustomerEntity

@Database(entities = [CustomerEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
}
