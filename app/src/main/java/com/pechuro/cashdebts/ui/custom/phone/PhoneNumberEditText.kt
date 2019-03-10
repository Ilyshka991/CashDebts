package com.pechuro.cashdebts.ui.custom.phone

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.custom.TextWatcher
import com.pechuro.cashdebts.ui.custom.hintedittext.HintEditText
import kotlinx.android.synthetic.main.layout_phone_edit_text.view.*

class PhoneNumberEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    var onDoneClick: () -> Unit = {}

    private lateinit var textCode: EditText
    private lateinit var textNumber: HintEditText

    private val listeners = mutableListOf<PhoneTextWatcher>()
    private val codeWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty()) {
                textCode.setText("+")
                moveCodeCursorAtTheEnd()
            }
            listeners.forEach {
                it.onCodeChanged(s.toString())
            }
        }
    }
    private val numberWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listeners.forEach {
                it.onNumberChanged(s?.toString())
            }
        }
    }

    init {
        inflate(context, R.layout.layout_phone_edit_text, this)
        textCode = text_code.apply {
            addTextChangedListener(codeWatcher)
            setText("+")
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    textNumber.requestFocus()
                    return@setOnEditorActionListener true
                }
                false
            }
        }
        textNumber = text_number.apply {
            addTextChangedListener(numberWatcher)
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onDoneClick()
                    return@setOnEditorActionListener true
                }
                false
            }
        }
    }

    fun addListener(listener: PhoneTextWatcher) {
        listeners.add(listener)
    }

    fun setCountryData(data: CountryData?) {
        data?.let {
            textCode.setText(it.phonePrefix)
            moveCodeCursorAtTheEnd()
        }
        textNumber.hintText = data?.phonePattern
    }

    fun getPhoneNumber() = textCode.text.toString() + textNumber.getEnteredText()

    private fun moveCodeCursorAtTheEnd() {
        textCode.setSelection(textCode.text.length)
    }
}
