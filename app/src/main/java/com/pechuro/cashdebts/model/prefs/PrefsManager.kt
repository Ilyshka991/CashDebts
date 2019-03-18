package com.pechuro.cashdebts.model.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class PrefsManager @Inject constructor(private val prefs: SharedPreferences) {

    var isUserAddInfo: Boolean
        get() = prefs.getBoolean(PrefsKey.IS_USER_ADD_INFO, false)
        set(value) = prefs.edit {
            putBoolean(PrefsKey.IS_USER_ADD_INFO, value)
        }
}