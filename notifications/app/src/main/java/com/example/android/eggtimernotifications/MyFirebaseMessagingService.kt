package com.example.android.eggtimernotifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService()
{
    override fun onNewToken(token: String?)
    {
        Log.i("Felo", "Refresh Token: $token")

        //sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage?)
    {

    }
}