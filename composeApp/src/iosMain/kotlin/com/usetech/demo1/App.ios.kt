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

@Composable
actual fun getStartDestination(): String {
    val shouldOpenDetails = IOSNotificationState.shouldOpenDetails

    // Réinitialiser après lecture
    LaunchedEffect(shouldOpenDetails) {
        if (shouldOpenDetails) {
            IOSNotificationState.shouldOpenDetails = false
        }
    }

    return if (shouldOpenDetails) {
        Screen.NotificationDetails.route
    } else {
        Screen.Home.route
    }
}