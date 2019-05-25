package com.pechuro.cashdebts.di.module

import com.pechuro.cashdebts.broadcast.notificationactions.NotificationActionsBroadcastReceiver
import com.pechuro.cashdebts.di.annotations.ServiceScope
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface AppBroadcastReceiversModule {

    @ServiceScope
    @ContributesAndroidInjector
    fun bindNotificationActions(): NotificationActionsBroadcastReceiver
}