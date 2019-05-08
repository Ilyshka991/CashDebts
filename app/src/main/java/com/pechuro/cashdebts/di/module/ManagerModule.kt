package com.pechuro.cashdebts.di.module

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import com.pechuro.cashdebts.di.annotations.AppScope
import dagger.Module
import dagger.Provides

@Module
class ManagerModule {

    @Provides
    @AppScope
    fun provideTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    @AppScope
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @AppScope
    fun provideInputMethodManager(context: Context): InputMethodManager {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    @Provides
    @AppScope
    fun provideNotificationManager(context: Context) = NotificationManagerCompat.from(context)
}