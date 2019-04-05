package com.pechuro.cashdebts.data.di.module

import com.pechuro.cashdebts.data.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.repositories.impl.AuthRepositoryImpl
import com.pechuro.cashdebts.data.data.repositories.impl.DebtRepositoryImpl
import com.pechuro.cashdebts.data.data.repositories.impl.StorageRepositoryImpl
import com.pechuro.cashdebts.data.data.repositories.impl.UserRepositoryImpl
import com.pechuro.cashdebts.data.di.scopes.DataScope
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