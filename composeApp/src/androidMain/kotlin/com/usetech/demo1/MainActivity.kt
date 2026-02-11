package com.usetech.demo1

import androidx.activity.compose.setContent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {

    companion object {
        var shouldOpenDetails = mutableStateOf(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser le contexte global
        AndroidApp.init(this)

        // Vérifier si on vient d'une notification
        val openDetails = intent?.getBooleanExtra("OPEN_DETAILS", false) ?: false
        shouldOpenDetails.value = openDetails

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        // Gérer le cas où l'app est déjà ouverte
        val openDetails = intent.getBooleanExtra("OPEN_DETAILS", false)
        shouldOpenDetails.value = openDetails
    }
}