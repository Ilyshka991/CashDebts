package com.pechuro.cashdebts.ui.custom.phone

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.pechuro.cashdebts.model.entity.CountryData

@BindingAdapter("country_data")
fun setCountryData(view: PhoneNumberEditText, data: CountryData?) {
    view.setCountryData(data)
}


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
    val listener = object : PhoneTextWatcher {
        override fun onCodeChanged(code: String?) {
            attrChange.onChange()
        }

        override fun onNumberChanged(number: String?) {
            attrChange.onChange()
        }

    }
    view.addListener(listener)
}