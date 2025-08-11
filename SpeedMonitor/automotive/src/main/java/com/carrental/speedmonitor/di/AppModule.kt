package com.carrental.speedmonitor.di

import android.car.Car
import android.car.hardware.property.CarPropertyManager
import android.content.Context
import androidx.room.Room
import com.carrental.speedmonitor.data.local.AppDatabase
import com.carrental.speedmonitor.data.local.dao.CustomerDao
import com.carrental.speedmonitor.domain.notification.AwsSender
import com.carrental.speedmonitor.domain.notification.FirebaseSender
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "car_rental_db").build()

    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Provides
    fun provideFirebaseSender(): FirebaseSender = FirebaseSender()

    @Provides
    fun provideAwsSender(): AwsSender = AwsSender()

    @Provides
    @Singleton
    fun provideCarPropertyManager(@ApplicationContext context: Context): CarPropertyManager {
        val car = Car.createCar(context)
        car.connect()
        return car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance()
}
