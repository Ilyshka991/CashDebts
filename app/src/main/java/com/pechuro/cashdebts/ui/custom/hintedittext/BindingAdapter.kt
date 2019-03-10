package com.pechuro.cashdebts.ui.custom.hintedittext

import android.text.Editable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.pechuro.cashdebts.ui.custom.TextWatcher


@InverseBindingAdapter(attribute = "hint_text")
fun getPhoneNumber(view: HintEditText): String {
    return view.getEnteredText()
}

@BindingAdapter("hint_text")
fun setPhoneNumber(view: HintEditText, value: String?) {
//Stub
}

@BindingAdapter("app:hint_textAttrChanged")
fun setListeners(
    view: HintEditText,
    attrChange: InverseBindingListener
) {
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            attrChange.onChange()
        }
    }
    view.addTextChangedListener(listener)
}
