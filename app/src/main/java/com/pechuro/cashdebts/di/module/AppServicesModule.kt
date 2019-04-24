package com.pechuro.cashdebts.di.module

import com.pechuro.cashdebts.di.annotations.ServiceScope
import com.pechuro.cashdebts.service.fcm.FCMService
import com.pechuro.cashdebts.service.fcm.FCMServiceModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface AppServicesModule {

    @ServiceScope
    @ContributesAndroidInjector(modules = [FCMServiceModule::class])
    fun bindFcm(): FCMService
}