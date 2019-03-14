package com.pechuro.cashdebts.ui.activity.main

import com.pechuro.cashdebts.data.repositories.FirebaseAuthRepository
import com.pechuro.cashdebts.ui.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository) : BaseViewModel() {

    fun logout() {
        authRepository.signOut()
    }

}
