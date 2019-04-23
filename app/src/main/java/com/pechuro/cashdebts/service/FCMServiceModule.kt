package com.pechuro.cashdebts.service

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.pechuro.cashdebts.di.annotations.ServiceScope
import com.pechuro.cashdebts.model.notification.NotificationManager
import dagger.Module
import dagger.Provides

@Module
class FCMServiceModule {

    @Provides
    @ServiceScope
    fun provideNotificationManager(
        context: Context,
        notificationManager: NotificationManagerCompat
    ) = NotificationManager(context, notificationManager)
}