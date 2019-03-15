package com.pechuro.cashdebts.di.component

import com.pechuro.cashdebts.data.repositories.FirebaseStorageRepository
import com.pechuro.cashdebts.data.repositories.FirestoreDebtRepository
import com.pechuro.cashdebts.data.repositories.FirestoreUserRepository
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.di.module.FirebaseModule
import com.pechuro.cashdebts.di.module.RepositoriesModule
import com.pechuro.cashdebts.di.scopes.DataScope
import dagger.Component

@DataScope
@Component(modules = [FirebaseModule::class, RepositoriesModule::class])
interface DataComponent {

    fun authRepository(): IAuthRepository

    fun storageRepository(): FirebaseStorageRepository

    fun debtRepository(): FirestoreDebtRepository

    fun userRepository(): FirestoreUserRepository

}
