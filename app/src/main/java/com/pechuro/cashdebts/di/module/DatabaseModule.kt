package com.pechuro.cashdebts.di.module

import android.content.Context
import androidx.room.Room
import com.pechuro.cashdebts.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, "database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDebtDao(appDatabase: AppDatabase) = appDatabase.debtDao()
}