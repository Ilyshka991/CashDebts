package com.pechuro.cashdebts.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.local.database.dao.DebtDao
import com.pechuro.cashdebts.data.remote.FirebaseInteractor
import com.pechuro.cashdebts.data.remote.FirestoreRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestoreRepository(dao: DebtDao, store: FirebaseFirestore, auth: FirebaseAuth) =
        FirestoreRepository(dao, store, auth)

    @Provides
    @Singleton
    fun provideFirestoreInteractor(repository: FirestoreRepository, auth: FirebaseAuth) =
        FirebaseInteractor(repository, auth)

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth() = FirebaseAuth.getInstance()
}