package com.pechuro.cashdebts.data.di.module

import com.pechuro.cashdebts.data.data.repositories.*
import com.pechuro.cashdebts.data.data.repositories.impl.*
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
    fun provideLocalDebtRepository(repository: LocalDebtRepositoryImpl): ILocalDebtRepository

    @Binds
    @DataScope
    fun provideRemoteDebtRepository(repository: RemoteDebtRepositoryImpl): IRemoteDebtRepository

    @Binds
    @DataScope
    fun provideMessagingRepository(repository: MessagingRepositoryImpl): IMessagingRepository

    @Binds
    @DataScope
    fun provideVersionRepository(repository: VersionRepositoryImpl): IVersionRepository
}