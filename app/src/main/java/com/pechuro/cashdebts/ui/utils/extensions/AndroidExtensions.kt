package com.pechuro.cashdebts.ui.utils.extensions

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import android.telephony.TelephonyManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.transaction(
    now: Boolean = false,
    allowStateLoss: Boolean = false,
    body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction().apply {
        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    transaction.body()
    if (now) {
        if (allowStateLoss) {
            transaction.commitNowAllowingStateLoss()
        } else {
            transaction.commitNow()
        }
    } else {
        if (allowStateLoss) {
            transaction.commitAllowingStateLoss()
        } else {
            transaction.commit()
        }
    }
}

fun TelephonyManager.getUserCountryCode(): String? {
    val simCountry = simCountryIso
    if (simCountry != null && simCountry.length == 2) {
        return simCountry.toUpperCase()
    } else if (phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
        val networkCountry = networkCountryIso
        if (networkCountry != null && networkCountry.length == 2) {
            return networkCountry.toUpperCase()
        }
    }
    return null
}

val Int.px: Int
    get() = Math.round(this * Resources.getSystem().displayMetrics.density)

val Context.defaultSharedPreferences: SharedPreferences
    get() = getSharedPreferences(packageName, MODE_PRIVATE)