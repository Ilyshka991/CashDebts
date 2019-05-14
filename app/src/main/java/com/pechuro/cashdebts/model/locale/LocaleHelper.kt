package com.pechuro.cashdebts.model.locale

import android.content.Context
import android.content.res.Configuration
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.utils.extensions.defaultSharedPreferences
import java.util.*

object LocaleManager {

    fun updateLocale(context: Context): Context {
        val prefsManager = PrefsManager(context.defaultSharedPreferences)
        val locale = prefsManager.settingCurrentLocale
        return if (locale.isEmpty()) context else setNewLocale(context, locale)
    }

    private fun setNewLocale(context: Context, language: String): Context {
        val locale = Locale(language).also {
            Locale.setDefault(it)
        }
        val config = Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }
}