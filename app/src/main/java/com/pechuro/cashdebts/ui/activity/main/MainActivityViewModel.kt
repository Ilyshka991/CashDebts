package com.pechuro.cashdebts.ui.activity.main

import com.pechuro.cashdebts.data.repositories.impl.AuthRepositoryImpl
import com.pechuro.cashdebts.ui.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val authRepository: AuthRepositoryImpl) : BaseViewModel() {

    fun logout() {
        authRepository.signOut()
    }

}
