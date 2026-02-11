package com.usetech.demo1

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotifyMeButton() {
    val notificationManager = remember { NotificationManager() }
    var permissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        notificationManager.requestPermission { granted ->
            permissionGranted = granted
        }
    }

    Button(
        onClick = {
            if (permissionGranted) {
                notificationManager.sendNotification(
                    title = "Notification !",
                    message = "Tu as cliqué sur Notify Me 🔔"
                )
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text("Notify Me")
    }
}