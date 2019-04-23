package com.pechuro.cashdebts.data.di.component

import com.pechuro.cashdebts.data.data.repositories.*
import com.pechuro.cashdebts.data.di.module.FirebaseModule
import com.pechuro.cashdebts.data.di.module.RepositoriesModule
import com.pechuro.cashdebts.data.di.scopes.DataScope
import dagger.Component

@DataScope
@Component(modules = [FirebaseModule::class, RepositoriesModule::class])
interface DataComponent {

    fun authRepository(): IAuthRepository

    fun storageRepository(): IStorageRepository

    fun localDebtRepository(): ILocalDebtRepository

    fun remoteDebtRepository(): IRemoteDebtRepository

    fun userRepository(): IUserRepository

    fun messagingRepository(): IMessagingRepository
}
