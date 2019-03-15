package com.pechuro.cashdebts.di.module

import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.impl.AuthRepositoryImpl
import com.pechuro.cashdebts.di.scopes.DataScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {

    @Binds
    @DataScope
    fun provideAuth(repository: AuthRepositoryImpl): IAuthRepository
}