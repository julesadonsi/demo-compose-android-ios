package com.usetech.demo1

expect class NotificationManager() {
    fun sendNotification(title: String, message: String)
    fun requestPermission(onResult: (Boolean) -> Unit)
}