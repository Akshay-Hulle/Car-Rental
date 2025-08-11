package com.carrental.speedmonitor.data.remote

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCustomerService @Inject constructor(
    private val database: FirebaseDatabase
) {
    suspend fun pushCustomerSpeedData(
        customerId: String,
        data: Map<String, Any?>
    ): Boolean {
        return try {
            database.getReference("customers")
                .child(customerId)
                .setValue(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Speeding event with ID $customerId successfully logged for rental ${customerId}.")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to log speeding event: $customerId", e)
                }
                .await() // suspends until complete
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
