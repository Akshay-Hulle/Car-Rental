package com.carrental.speedmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrental.speedmonitor.data.remote.FirebaseCustomerService
import com.carrental.speedmonitor.data.repository.CustomerRepository
import com.carrental.speedmonitor.domain.model.CustomerSettings
import com.carrental.speedmonitor.domain.notification.SpeedNotifier
import com.carrental.speedmonitor.framework.notification.CarSpeedNotifier
import com.carrental.speedmonitor.framework.sensor.SpeedMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel responsible for:
 * - Fetching customer settings (max speed, name, etc.)
 * - Starting and stopping vehicle speed monitoring (real or mock)
 * - Handling speed limit violations by sending notifications to:
 *   1. Car infotainment
 *   2. Remote server (Firebase)
 */
@HiltViewModel
class SpeedMonitorViewModel @Inject constructor(
    private val repository: CustomerRepository,
    private val notifier: SpeedNotifier,    // for cloud notifications
    private val speedMonitor: SpeedMonitor,
    private val firebaseService: FirebaseCustomerService,
    private val carNotifier: CarSpeedNotifier // for infotainment
) : ViewModel() {

    private var hasAlerted = false

    private val _currentSpeed = MutableStateFlow(0f)
    val currentSpeed: StateFlow<Float> = _currentSpeed

    private val _customer = MutableStateFlow<CustomerSettings?>(null)
    val customer: StateFlow<CustomerSettings?> = _customer

    private val _useMockSpeed = MutableStateFlow(false)
    val useMockSpeed: StateFlow<Boolean> = _useMockSpeed

//    Enables or disables mock speed mode and restarts monitoring.
    fun setUseMockSpeed(enabled: Boolean) {
        _useMockSpeed.value = enabled
        restartMonitoring()
    }


//     Loads customer settings from the repository and starts monitoring speed.
    fun startMonitoring(customerId: String) {
        viewModelScope.launch {
            val customerSettings = repository.getCustomerSettings(customerId)
            _customer.value = customerSettings
            restartMonitoring()
        }
    }

    /**
     * Restarts the speed monitoring service (real or mock)
     * based on the current `_useMockSpeed` value.
     */
    private fun restartMonitoring() {
        speedMonitor.startSpeedUpdates(_useMockSpeed.value) { speed ->
            _currentSpeed.value = speed
            handleSpeedChange(speed)
        }
    }

    /**
     * Handles speed change events:
     * - Checks if the speed exceeds the customer's limit
     * - Sends data to Firebase
     * - Triggers car infotainment and push notifications
     */
    private fun handleSpeedChange(speed: Float) {
        val customer = _customer.value ?: return

        if (speed > customer.maxSpeedLimit) {
            // Prepare data for Firebase
            val customerData = mapOf(
            "id" to customer.id,
            "name" to customer.name,
            "maxSpeedLimit" to customer.maxSpeedLimit,
            "exceedSpeeds" to speed
            )

            // Push exceeded speed data to Firebase
            viewModelScope.launch {
                val success = firebaseService.pushCustomerSpeedData(customer.id, customerData)
                if (!success) {
                    // Optional: retry or show error notification
                }
            }


            if (!hasAlerted) {
                // Send notification to car infotainment
                carNotifier.sendSpeedExceededNotification(
                    customerName = customer.name,
                    currentSpeed = speed,
                    maxLimit = customer.maxSpeedLimit
                )

                // Send cloud push notification
                viewModelScope.launch {
                    notifier.notify(customer, speed)
                    hasAlerted = true
                }
            }
        } else {
            hasAlerted = false
        }
    }
}
