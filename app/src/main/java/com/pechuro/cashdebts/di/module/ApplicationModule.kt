package com.pechuro.cashdebts.di.module

import android.content.Context
import com.pechuro.cashdebts.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(app: App): Context = app
}