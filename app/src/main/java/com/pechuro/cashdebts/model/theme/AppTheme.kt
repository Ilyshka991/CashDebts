package com.pechuro.cashdebts.model.theme

import androidx.appcompat.app.AppCompatDelegate

object AppTheme {

    fun setTheme(theme: String) {
        val mode = when (theme) {
            "auto" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> throw IllegalArgumentException("Unknown theme")
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

}