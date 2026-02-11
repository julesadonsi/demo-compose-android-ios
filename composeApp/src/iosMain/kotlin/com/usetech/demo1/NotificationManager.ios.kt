package com.usetech.demo1

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.Foundation.NSUUID

actual class NotificationManager actual constructor() {

    actual fun sendNotification(title: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setSound(UNNotificationSound.defaultSound)
            // Ajouter une catégorie pour identifier la notification
            setCategoryIdentifier("DETAILS_CATEGORY")
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
            timeInterval = 0.1,
            repeats = false
        )

        val identifier = NSUUID().UUIDString
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = identifier,
            content = content,
            trigger = trigger
        )

        UNUserNotificationCenter.currentNotificationCenter()
            .addNotificationRequest(request) { error ->
                if (error != null) {
                    println("❌ Erreur notification iOS: ${error.localizedDescription}")
                } else {
                    println("✅ Notification iOS envoyée avec succès")
                }
            }
    }

    actual fun requestPermission(onResult: (Boolean) -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.getNotificationSettingsWithCompletionHandler { settings ->
            if (settings != null) {
                val authStatus = settings.authorizationStatus

                if (authStatus == 2L) {
                    println("✅ Permission iOS déjà accordée")
                    onResult(true)
                    return@getNotificationSettingsWithCompletionHandler
                }
            }

            center.requestAuthorizationWithOptions(
                options = UNAuthorizationOptionAlert or
                        UNAuthorizationOptionSound or
                        UNAuthorizationOptionBadge
            ) { granted, error ->
                if (error != null) {
                    println("❌ Erreur permission iOS: ${error.localizedDescription}")
                    onResult(false)
                } else {
                    println("✅ Permission iOS: $granted")
                    onResult(granted)
                }
            }
        }
    }
}

// Objet pour gérer l'état du deep linking iOS
object IOSNotificationState {
    var shouldOpenDetails: Boolean = false
}