package com.pechuro.cashdebts.ui.custom.phone

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener


@InverseBindingAdapter(attribute = "phone_number")
fun getPhoneNumber(view: PhoneNumberEditText): String {
    return view.getPhoneNumber()
}

@BindingAdapter("phone_number")
fun setPhoneNumber(view: PhoneNumberEditText, value: String?) {
//Stub
}

@BindingAdapter("app:phone_numberAttrChanged")
fun setListeners(
    view: PhoneNumberEditText,
    attrChange: InverseBindingListener
) {
    val listener = object : PhoneNumberEditText.PhoneTextWatcher {
        override fun onCodeChanged() {
            attrChange.onChange()
        }

        override fun onNumberChanged() {
            attrChange.onChange()
        }
    }
    view.addListener(listener)
}