package com.pechuro.cashdebts.di.module

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface AppBroadcastReceiversModule