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

    var filterUnitePersons: Boolean
        get() = prefs.getBoolean(PrefsKey.FILTER_UNITE_PERSONS, false)
        set(value) = prefs.edit {
            putBoolean(PrefsKey.FILTER_UNITE_PERSONS, value)
        }
    var filterNotShowCompleted: Boolean
        get() = prefs.getBoolean(PrefsKey.FILTER_NOT_SHOW_COMPLETED, false)
        set(value) = prefs.edit {
            putBoolean(PrefsKey.FILTER_NOT_SHOW_COMPLETED, value)
        }

    var settingsAutoAddPlus: Boolean
        get() = prefs.getBoolean(PrefsKey.SETTING_AUTO_ADD_PLUS, true)
        set(value) = prefs.edit {
            putBoolean(PrefsKey.SETTING_AUTO_ADD_PLUS, value)
        }
    var settingCurrentLocale: String
        get() = prefs.getString(PrefsKey.SETTING_CURRENT_LOCALE, "system") ?: "system"
        set(value) = prefs.edit {
            putString(PrefsKey.SETTING_CURRENT_LOCALE, value)
        }
}