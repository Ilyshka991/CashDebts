package com.pechuro.cashdebts.ui.activity.main

import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val prefsManager: PrefsManager
) : BaseViewModel() {

    fun logout() {
        authRepository.signOut()
        prefsManager.isUserAddInfo = false
    }

    fun isUserAddInfo() = prefsManager.isUserAddInfo
}
