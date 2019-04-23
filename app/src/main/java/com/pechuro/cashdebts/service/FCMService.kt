package com.pechuro.cashdebts.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pechuro.cashdebts.model.notification.NotificationManager

class FCMService : FirebaseMessagingService() {
    private val notificationManager by lazy {
        NotificationManager(this.applicationContext)
    }

    override fun onMessageReceived(msg: RemoteMessage?) {
        notificationManager.showDebtAddNotification()
    }
}