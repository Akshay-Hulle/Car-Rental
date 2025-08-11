package com.carrental.speedmonitor.framework.sensor

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SpeedMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var car: Car? = null
    private var carPropertyManager: CarPropertyManager? = null
    private var onSpeedChangedCallback: ((Float) -> Unit)? = null

    private var mockThread: Thread? = null
    private var isMocking = false

    /**
     * Starts listening for speed updates.
     * If `mock` is true → generates random speed values.
     * If `mock` is false → listens to real car speed from the vehicle property manager.
     */
    fun startSpeedUpdates(mock: Boolean, onSpeedChanged: (Float) -> Unit) {
        this.onSpeedChangedCallback = onSpeedChanged

        if (mock) {
            stopCarSpeedUpdates()
            startMockSpeed()
        } else {
            stopMockSpeed()
            startCarSpeedUpdates()
        }
    }

    /**
     * Connects to the Android Automotive `Car` service
     * and starts receiving real vehicle speed updates.
     */
    private fun startCarSpeedUpdates() {
        try {
            car = Car.createCar(context, serviceConnection)
            car?.connect()
            println("Connecting to Car service for real speed...")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Car connection failed: ${e.message}")
        }
    }

    /**
     * Stops receiving real car speed updates
     * and disconnects from the `Car` service.
     */
    private fun stopCarSpeedUpdates() {
        try {
            carPropertyManager?.unregisterCallback(carSpeedCallback)
        } catch (_: Exception) {}
        try {
            car?.disconnect()
        } catch (_: Exception) {}
        carPropertyManager = null
        car = null
    }

    /**
     * Handles connection lifecycle to the Car service.
     * This is called when `car.connect()` succeeds or disconnects.
     */
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            try {
                carPropertyManager = car?.getCarManager(Car.PROPERTY_SERVICE) as? CarPropertyManager
                if (carPropertyManager == null) return

                carPropertyManager?.registerCallback(
                    carSpeedCallback,
                    VehiclePropertyIds.PERF_VEHICLE_SPEED,
                    CarPropertyManager.SENSOR_RATE_ONCHANGE
                )

                println("Car speed listener registered.")
            } catch (e: Exception) {
                println("Error registering car speed listener: ${e.message}")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            carPropertyManager = null
        }
    }

    /**
     * Callback that receives updates from the CarPropertyManager.
     * Listens for `PERF_VEHICLE_SPEED` changes and calls the provided callback.
     */
    private val carSpeedCallback = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(event: CarPropertyValue<*>) {
            if (event.propertyId == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                val speed = (event.value as? Float) ?: return
                onSpeedChangedCallback?.invoke(speed)
            }
        }

        override fun onErrorEvent(propId: Int, zone: Int) {}
    }

    /**
     * Starts generating fake speed values in a separate thread.
     * Useful for testing when real car data is unavailable.
     */
    private fun startMockSpeed() {
        stopMockSpeed()
        isMocking = true
        mockThread = Thread {
            try {
                var speed: Float
                while (isMocking) {
                    speed = (20..155).random().toFloat()
                    onSpeedChangedCallback?.invoke(speed)
                    Thread.sleep(3000)
                }
            } catch (e: InterruptedException) {
                // Thread interrupted -> exit quietly
            }
        }.apply { start() }
    }

    /**
     * Stops mock speed generation and kills the thread.
     */
    private fun stopMockSpeed() {
        onSpeedChangedCallback?.invoke(0f)
        isMocking = false
        mockThread?.interrupt()
        mockThread = null
    }
}

