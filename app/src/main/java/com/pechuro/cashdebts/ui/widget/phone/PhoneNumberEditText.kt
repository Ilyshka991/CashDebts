package com.pechuro.cashdebts.ui.widget.phone

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.widget.TextWatcher
import com.pechuro.cashdebts.ui.widget.hintedittext.HintEditText
import kotlinx.android.synthetic.main.layout_phone_edit_text.view.*

class PhoneNumberEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var onDoneClick: () -> Unit = {}
    var onCountryChanged: ((CountryData) -> Unit)? = null

    var countryList: List<CountryData>? = null

    lateinit var textCode: EditText
    val textNumber: HintEditText

    var countryData = CountryData.EMPTY
        set(value) {
            field = value
            onCountryChanged?.invoke(value)
            if (!value.isEmpty) {
                textCode.setText(value.phonePrefix)
                moveCodeCursorAtTheEnd()
            }
            textNumber.hintText = value.phonePattern
        }

    private val codeWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty()) {
                textCode.setText("+")
                moveCodeCursorAtTheEnd()
            } else {
                val country = countryList?.findLast { it.phonePrefix == s.toString() }
                if (countryData != country) {
                    countryData = country ?: CountryData.EMPTY
                }
            }
        }
    }

    init {
        inflate(context, R.layout.layout_phone_edit_text, this)
        textNumber = text_number.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onDoneClick()
                    return@setOnEditorActionListener true
                }
                false
            }
        }
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
    }

    fun getPhoneNumber() = textCode.text.toString() + textNumber.getEnteredText()

    fun setPhoneNumber(number: String): Boolean {
        val pattern = PHONE_REGEX.toRegex()
        if (!pattern.matches(number)) return false

        var countryData: CountryData? = null
        for (i in 4 downTo 1) {
            val possiblePrefix = number.slice(0..i)
            val possibleData = countryList?.findLast { possiblePrefix == it.phonePrefix }
            if (possibleData != null) {
                countryData = possibleData
                break
            }
        }

        if (countryData == null) {
            return false
        }

        this.countryData = countryData
        val numberWithoutPrefix = number.removePrefix(countryData.phonePrefix)
        textNumber.text.clear()
        numberWithoutPrefix.forEach {
            textNumber.text.append(it)
        }

        return true
    }

    override fun setEnabled(isEnabled: Boolean) {
        textCode.isFocusable = isEnabled
        textNumber.isFocusable = isEnabled
    }

    private fun moveCodeCursorAtTheEnd() {
        textCode.setSelection(textCode.text.length)
    }

    companion object {
        private const val PHONE_REGEX = """\+[0-9]{3,}"""
    }
}
