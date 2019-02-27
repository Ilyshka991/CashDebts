package com.pechuro.cashdebts.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.local.database.dao.DebtDao
import com.pechuro.cashdebts.data.remote.FirestoreRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestoreSync(dao: DebtDao, fireStore: FirebaseFirestore) = FirestoreRepository(dao, fireStore)

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()
}