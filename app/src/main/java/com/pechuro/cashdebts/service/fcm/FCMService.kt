package com.pechuro.cashdebts.service.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pechuro.cashdebts.data.data.repositories.IMessagingRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.notification.NotificationManager
import dagger.android.AndroidInjection
import javax.inject.Inject

class FCMService : FirebaseMessagingService() {

    @Inject
    protected lateinit var notificationManager: NotificationManager
    @Inject
    protected lateinit var messagingRepository: IMessagingRepository
    @Inject
    protected lateinit var userRepository: IUserRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
    }

    override fun onNewToken(token: String) {
        messagingRepository
            .saveToken(userRepository.currentUserBaseInformation.uid, token)
            .subscribe()
    }

    override fun onMessageReceived(msg: RemoteMessage?) {
        val notificationData = msg?.data ?: return
        notificationManager.show(notificationData)
    }
}