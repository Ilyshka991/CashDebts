package com.pechuro.cashdebts.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pechuro.cashdebts.di.scopes.DataScope
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    @DataScope
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @DataScope
    fun provideAuth() = FirebaseAuth.getInstance()

    @Provides
    @DataScope
    fun provideStorage() = FirebaseStorage.getInstance()

    @Provides
    @DataScope
    fun providePhoneAuthProvider() = PhoneAuthProvider.getInstance()
}