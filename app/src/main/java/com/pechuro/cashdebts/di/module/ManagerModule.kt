package com.pechuro.cashdebts.di.module

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
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
}