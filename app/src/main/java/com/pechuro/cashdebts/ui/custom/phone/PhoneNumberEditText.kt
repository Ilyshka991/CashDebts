package com.pechuro.cashdebts.ui.custom.phone

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.custom.TextWatcher
import kotlinx.android.synthetic.main.layout_phone_edit_text.view.*

class PhoneNumberEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val textCode: EditText
    private val textNumber: HintEditText

    private val listeners = mutableListOf<PhoneTextWatcher>()
    private val codeWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listeners.forEach {
                it.onCodeChanged()
            }
        }
    }
    private val numberWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listeners.forEach {
                it.onNumberChanged()
            }
        }
    }

    init {
        inflate(context, R.layout.layout_phone_edit_text, this)
        textCode = text_code.apply {
            addTextChangedListener(codeWatcher)
        }
        textNumber = text_number.apply {
            addTextChangedListener(numberWatcher)
        }
    }

    fun addListener(listener: PhoneTextWatcher) {
        listeners.add(listener)
    }

    @SuppressLint("SetTextI18n")
    fun setCountryData(data: CountyData) {
        textCode.setText("+${data.phonePrefix}")
        textNumber.hintText = data.phonePattern
    }

    fun getPhoneNumber(): String {
        val number = StringBuilder()
        if (textCode.text.isNotEmpty()) {
            number.append(textCode.text.substring(1))
        }
        number.append(
            textNumber.text.replace(Regex("[ ]"), "")
        )
        return number.toString()
    }


    interface PhoneTextWatcher {
        fun onCodeChanged()

        fun onNumberChanged()
    }
}
