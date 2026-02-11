package com.usetech.demo1

import androidx.compose.ui.window.ComposeUIViewController
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.UserNotifications.UNNotificationResponse
import platform.darwin.NSObject

fun MainViewController() = ComposeUIViewController {
    val delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            didReceiveNotificationResponse: UNNotificationResponse,
            withCompletionHandler: () -> Unit
        ) {
            println("✅ Notification iOS cliquée !")
            IOSNotificationState.shouldOpenDetails = true
            withCompletionHandler()
        }

        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            willPresentNotification: platform.UserNotifications.UNNotification,
            withCompletionHandler: (platform.UserNotifications.UNNotificationPresentationOptions) -> Unit
        ) {
            // Afficher la notification même quand l'app est au premier plan
            withCompletionHandler(
                platform.UserNotifications.UNNotificationPresentationOptionAlert or
                        platform.UserNotifications.UNNotificationPresentationOptionSound or
                        platform.UserNotifications.UNNotificationPresentationOptionBadge
            )
        }
    }

    UNUserNotificationCenter.currentNotificationCenter().delegate = delegate

    App()
}