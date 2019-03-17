package com.pechuro.cashdebts.ui.activity.main

import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) : BaseViewModel() {

    fun logout() {
        authRepository.signOut()
    }

    private fun isUserAdded() {
        // userRepository.isUserExist()
    }
}
