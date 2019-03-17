package com.pechuro.cashdebts.ui.activity.main

import android.content.SharedPreferences
import androidx.core.content.edit
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.prefs.PrefsKey
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val prefs: SharedPreferences
) : BaseViewModel() {

    fun logout() {
        authRepository.signOut()
        prefs.edit {
            putBoolean(PrefsKey.IS_USER_ADD_INFO, false)
        }
    }

    fun isUserAddInfo() = prefs.getBoolean(PrefsKey.IS_USER_ADD_INFO, false)
}
