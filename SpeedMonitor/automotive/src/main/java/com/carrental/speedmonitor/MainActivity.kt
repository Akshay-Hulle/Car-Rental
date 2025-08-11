package com.carrental.speedmonitor

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.carrental.speedmonitor.presentation.ui.SpeedAlertScreen
import com.carrental.speedmonitor.presentation.viewmodel.SpeedMonitorViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SpeedMonitorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        val customerId = "user123"
        viewModel.startMonitoring(customerId)

        setContent {
            val customer by viewModel.customer.collectAsState()
            val currentSpeed by viewModel.currentSpeed.collectAsState()
            val useMockSpeed by viewModel.useMockSpeed.collectAsState()

            MaterialTheme {
                customer?.let {
                    SpeedAlertScreen(
                        customer = it,
                        currentSpeed = currentSpeed,
                        useMockSpeed = useMockSpeed,
                        onMockSpeedToggle = { enabled -> viewModel.setUseMockSpeed(enabled) }
                    )
                } ?: LoadingScreen()
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
