package com.usetech.demo1

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

actual class NotificationManager actual constructor() {

    private val channelId = "notify_me_channel"
    private val notificationId = 1

    private fun getContext(): Context {
        return AndroidApp.appContext
    }

    init {
        createNotificationChannel()
    }

    actual fun sendNotification(title: String, message: String) {
        val context = getContext()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
            println("✅ Notification Android envoyée")
        } catch (e: SecurityException) {
            println("❌ Erreur SecurityException: ${e.message}")
        }
    }

    actual fun requestPermission(onResult: (Boolean) -> Unit) {
        val context = getContext()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            onResult(hasPermission)
        } else {
            // Pas besoin de permission pour Android < 13
            onResult(true)
        }
    }

    // Fonction pour vérifier si la permission est accordée
    fun hasPermission(): Boolean {
        val context = getContext()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val context = getContext()
            val name = "Notifications"
            val descriptionText = "Canal pour les notifications de l'application"
            val importance = AndroidNotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as AndroidNotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

object AndroidApp {
    lateinit var appContext: Context
}