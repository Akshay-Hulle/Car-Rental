package com.carrental.speedmonitor.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.carrental.speedmonitor.domain.model.CustomerSettings

@Composable
fun SpeedAlertScreen(
    customer: CustomerSettings,
    currentSpeed: Float,
    useMockSpeed: Boolean,
    onMockSpeedToggle: (Boolean) -> Unit
) {
    val overLimit = currentSpeed > customer.maxSpeedLimit

    val bgColor = Color.White
    val textColor = if (overLimit) Color.Red else Color(0xFF2E7D32)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = if (overLimit){
                    "${customer.name} SLOW DOWN!"
                } else "${customer.name} You're Safe",
                fontSize = 36.sp,
                color = textColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Speed: ${String.format("%.1f", currentSpeed)} km/h",
                fontSize = 28.sp,
                color = Color.Black
            )

            Text(
                text = "Limit: ${customer.maxSpeedLimit} km/h",
                fontSize = 24.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = useMockSpeed,
                    onCheckedChange = { onMockSpeedToggle(it) }
                )
                Text(text = "Enable Mock Speed", color = Color.Black)
            }
        }
    }
}
