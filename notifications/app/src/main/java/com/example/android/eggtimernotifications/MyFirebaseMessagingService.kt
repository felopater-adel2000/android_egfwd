package com.example.android.eggtimernotifications

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.android.eggtimernotifications.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService()
{
    override fun onNewToken(token: String?)
    {
        Log.i("Felo", "Refresh Token: $token")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {

    }

    override fun onMessageReceived(message: RemoteMessage?)
    {
        Log.i("Felo", "From: ${message?.from}")

        message?.data.let {
            Log.i("Felo", "data: ${message?.data}")
        }


        message?.notification?.let {
            Log.d("Felo", "Message Notification Body: ${it.body}")
            sendNotification(it.body!!)
        }
    }

    private fun sendNotification(body: String)
    {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager

        notificationManager.sendNotification(body, applicationContext)
    }
}