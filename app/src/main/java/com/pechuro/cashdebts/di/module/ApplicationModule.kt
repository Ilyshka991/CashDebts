package com.pechuro.cashdebts.di.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.pechuro.cashdebts.App
import com.pechuro.cashdebts.di.annotations.AppScope
import com.pechuro.cashdebts.model.connectivity.ConnectivityListener
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @AppScope
    fun provideContext(app: App): Context = app

    @Provides
    @AppScope
    fun providePrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, MODE_PRIVATE)

    @Provides
    @AppScope
    fun provideConnectivityListener(manager: ConnectivityManager) = ConnectivityListener(manager)
}