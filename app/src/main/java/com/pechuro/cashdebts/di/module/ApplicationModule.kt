package com.pechuro.cashdebts.di.module

import android.content.Context
import android.telephony.TelephonyManager
import com.pechuro.cashdebts.App
import com.pechuro.cashdebts.di.annotations.AppScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @AppScope
    fun provideContext(app: App): Context = app

    @Provides
    fun provideTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }
}