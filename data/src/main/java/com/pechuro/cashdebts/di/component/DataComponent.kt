package com.pechuro.cashdebts.di.component

import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.di.module.FirebaseModule
import com.pechuro.cashdebts.di.module.RepositoriesModule
import com.pechuro.cashdebts.di.scopes.DataScope
import dagger.Component

@DataScope
@Component(modules = [FirebaseModule::class, RepositoriesModule::class])
interface DataComponent {

    fun authRepository(): IAuthRepository

    fun storageRepository(): IStorageRepository

    fun debtRepository(): IDebtRepository

    fun userRepository(): IUserRepository
}
