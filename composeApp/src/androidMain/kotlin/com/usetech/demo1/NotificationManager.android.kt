package com.usetech.demo1

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

        // Intent pour ouvrir l'app avec un flag spécial
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("OPEN_DETAILS", true) // Flag pour savoir qu'on vient d'une notification
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent) // Ajouter l'intent
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
            onResult(true)
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
    private var _appContext: Context? = null

    val appContext: Context
        get() = _appContext ?: error("Context not initialized")

    fun init(context: Context) {
        if (_appContext == null) {
            _appContext = context.applicationContext
        }
    }
}