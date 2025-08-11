package com.carrental.speedmonitor

import android.app.Application
import com.carrental.speedmonitor.data.repository.CustomerRepository
import com.carrental.speedmonitor.domain.model.CustomerSettings
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application(){
    @Inject
    lateinit var CustomerDB: CustomerRepository
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        // Add dummy customer rental config
        CoroutineScope(Dispatchers.IO).launch {
            CustomerDB.saveCustomer(
                CustomerSettings(
                    id = "user123",
                    name = "Akshay",
                    maxSpeedLimit = 50f,
                    notificationChannel = "FIREBASE"
                )
            )
        }
    }
}
