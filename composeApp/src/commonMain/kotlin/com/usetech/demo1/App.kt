package com.usetech.demo1


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
            // Navigation gère le démarrage
            val startDestination = getStartDestination()
            AppNavigation(startDestination = startDestination)
        }
    }
}

// Fonction expect pour gérer le deep linking
@Composable
expect fun getStartDestination(): String

@Composable
expect fun RequestNotificationPermission(
    onPermissionResult: (Boolean) -> Unit
)

@Composable
fun NotifyMeScreen(
    onNavigateToDetails: () -> Unit
) {
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
                // Envoyer notification avec action de navigation
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

                notificationManager.requestPermission { hasPermission ->
                    if (hasPermission) {
                        notificationManager.sendNotification(
                            title = "Notification !",
                            message = "Clique sur moi pour voir les détails 👆"
                        )
                        messageText = "✅ Notification envoyée ! Clique dessus"
                        showMessage = true
                        permissionGranted = true
                    } else {
                        shouldRequestPermission = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
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

        Spacer(Modifier.height(32.dp))

        // Bouton pour tester la navigation manuellement
        OutlinedButton(
            onClick = onNavigateToDetails,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aller aux détails (test)")
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Permission: ${if (permissionGranted) "✓ Accordée" else "✗ Non accordée"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun NotificationDetailsScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🔔",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Vous avez cliqué sur la notification",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("← Retour", style = MaterialTheme.typography.titleMedium)
        }
    }
}