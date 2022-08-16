package com.example.swith.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("doori","fmc token = $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("doori","fcm message notification = ${message.notification}")
        Log.e("doori","fcm message data = ${message.data}")
    }
}