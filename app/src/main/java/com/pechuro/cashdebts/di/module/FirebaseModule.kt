package com.pechuro.cashdebts.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pechuro.cashdebts.data.CurrentUser
import com.pechuro.cashdebts.data.FirebaseStorageRepository
import com.pechuro.cashdebts.data.FirestoreDebtRepository
import com.pechuro.cashdebts.data.FirestoreUserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestoreDebtRepository(store: FirebaseFirestore, auth: CurrentUser) =
        FirestoreDebtRepository(store, auth)

    @Provides
    @Singleton
    fun provideFirestoreUserRepository(store: FirebaseFirestore) =
        FirestoreUserRepository(store)

    @Provides
    @Singleton
    fun provideFirebaseStorageRepository(storage: FirebaseStorage) =
        FirebaseStorageRepository(storage)

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance()
}