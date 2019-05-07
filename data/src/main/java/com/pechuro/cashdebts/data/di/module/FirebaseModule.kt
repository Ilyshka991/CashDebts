package com.pechuro.cashdebts.data.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.pechuro.cashdebts.data.di.scopes.DataScope
import dagger.Module
import dagger.Provides

@Module
internal class FirebaseModule {

    @Provides
    @DataScope
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @DataScope
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @DataScope
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @DataScope
    fun providePhoneAuthProvider(): PhoneAuthProvider = PhoneAuthProvider.getInstance()

    @Provides
    @DataScope
    fun provideMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @DataScope
    fun provideInstanceId(): FirebaseInstanceId = FirebaseInstanceId.getInstance()
}