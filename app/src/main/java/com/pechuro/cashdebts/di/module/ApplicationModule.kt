package com.pechuro.cashdebts.di.module

import android.content.Context
import com.pechuro.cashdebts.App
import com.pechuro.cashdebts.di.annotations.AppScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @AppScope
    fun provideContext(app: App): Context = app
}