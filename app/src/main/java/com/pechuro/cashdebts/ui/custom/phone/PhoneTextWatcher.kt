package com.pechuro.cashdebts.ui.custom.phone

interface PhoneTextWatcher {
    fun onCodeChanged(code: String?) {}

    fun onNumberChanged(number: String?) {}
}