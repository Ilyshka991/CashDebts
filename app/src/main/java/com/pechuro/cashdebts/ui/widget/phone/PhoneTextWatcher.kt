package com.pechuro.cashdebts.ui.widget.phone

interface PhoneTextWatcher {
    fun onCodeChanged(code: String?) {}

    fun onNumberChanged(number: String?) {}
}