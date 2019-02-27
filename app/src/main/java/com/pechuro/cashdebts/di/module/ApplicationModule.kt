package com.pechuro.cashdebts.di.module

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.pechuro.cashdebts.App
import com.pechuro.cashdebts.data.model.CurrentUser
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(app: App): Context = app

    @Provides
    @Singleton
    fun provideCurrentUser(auth: FirebaseAuth) = CurrentUser(auth)
}