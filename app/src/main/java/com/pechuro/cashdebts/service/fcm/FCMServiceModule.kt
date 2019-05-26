package com.pechuro.cashdebts.service.fcm

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.pechuro.cashdebts.di.annotations.ServiceScope
import com.pechuro.cashdebts.model.notification.NotificationManager
import com.pechuro.cashdebts.model.prefs.PrefsManager
import dagger.Module
import dagger.Provides

@Module
class FCMServiceModule {

    @Provides
    @ServiceScope
    fun provideNotificationManager(
        context: Context,
        notificationManager: NotificationManagerCompat,
        prefsManager: PrefsManager
    ) = NotificationManager(context, notificationManager, prefsManager)
}