package com.usetech.demo1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NotifyMeScreen()
        }
    }
}

@Composable
expect fun RequestNotificationPermission(
    onPermissionResult: (Boolean) -> Unit
)

@Composable
fun NotifyMeScreen() {
    val notificationManager = remember { NotificationManager() }
    var permissionGranted by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var shouldRequestPermission by remember { mutableStateOf(false) }

    // Vérifier la permission au démarrage
    LaunchedEffect(Unit) {
        notificationManager.requestPermission { granted ->
            permissionGranted = granted
        }
    }

    // Demander la permission quand nécessaire
    if (shouldRequestPermission) {
        RequestNotificationPermission { granted ->
            permissionGranted = granted
            shouldRequestPermission = false

            if (granted) {
                notificationManager.sendNotification(
                    title = "Notification !",
                    message = "Tu as cliqué sur Notify Me 🔔"
                )
                messageText = "✅ Notification envoyée !"
            } else {
                messageText = "❌ Permission refusée"
            }
            showMessage = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Notifications Demo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = {
                showMessage = false

                // Vérifier si on a déjà la permission
                notificationManager.requestPermission { hasPermission ->
                    if (hasPermission) {
                        // Envoyer directement la notification
                        notificationManager.sendNotification(
                            title = "Notification !",
                            message = "Tu as cliqué sur Notify Me 🔔"
                        )
                        messageText = "✅ Notification envoyée !"
                        showMessage = true
                        permissionGranted = true
                    } else {
                        // Demander la permission
                        shouldRequestPermission = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("🔔 Notify Me", style = MaterialTheme.typography.titleMedium)
        }

        if (showMessage) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = messageText,
                color = if (permissionGranted)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Permission: ${if (permissionGranted) "✓ Accordée" else "✗ Non accordée"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}