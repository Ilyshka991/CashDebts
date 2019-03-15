package com.pechuro.cashdebts.di.module

import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.repositories.impl.AuthRepositoryImpl
import com.pechuro.cashdebts.data.repositories.impl.DebtRepositoryImpl
import com.pechuro.cashdebts.data.repositories.impl.StorageRepositoryImpl
import com.pechuro.cashdebts.data.repositories.impl.UserRepositoryImpl
import com.pechuro.cashdebts.di.scopes.DataScope
import dagger.Binds
import dagger.Module

@Module
internal interface RepositoriesModule {

    @Binds
    @DataScope
    fun provideAuth(repository: AuthRepositoryImpl): IAuthRepository

    @Binds
    @DataScope
    fun provideUser(repository: UserRepositoryImpl): IUserRepository

    @Binds
    @DataScope
    fun provideStorage(repository: StorageRepositoryImpl): IStorageRepository

    @Binds
    @DataScope
    fun provideDebt(repository: DebtRepositoryImpl): IDebtRepository
}