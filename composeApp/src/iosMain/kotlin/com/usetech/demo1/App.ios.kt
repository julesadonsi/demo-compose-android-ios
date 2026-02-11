package com.usetech.demo1

import androidx.compose.runtime.*

@Composable
actual fun RequestNotificationPermission(
    onPermissionResult: (Boolean) -> Unit
) {
    val notificationManager = remember { NotificationManager() }

    LaunchedEffect(Unit) {
        notificationManager.requestPermission { granted ->
            onPermissionResult(granted)
        }
    }
}