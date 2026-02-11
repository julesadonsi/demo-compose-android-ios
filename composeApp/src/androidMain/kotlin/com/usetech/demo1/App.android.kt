package com.usetech.demo1

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*

@Composable
actual fun RequestNotificationPermission(
    onPermissionResult: (Boolean) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            onPermissionResult(isGranted)
        }

        LaunchedEffect(Unit) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    } else {
        LaunchedEffect(Unit) {
            onPermissionResult(true)
        }
    }
}

@Composable
actual fun getStartDestination(): String {
    val shouldOpenDetails by MainActivity.shouldOpenDetails

    // Réinitialiser après lecture
    LaunchedEffect(shouldOpenDetails) {
        if (shouldOpenDetails) {
            MainActivity.shouldOpenDetails.value = false
        }
    }

    return if (shouldOpenDetails) {
        Screen.NotificationDetails.route
    } else {
        Screen.Home.route
    }
}