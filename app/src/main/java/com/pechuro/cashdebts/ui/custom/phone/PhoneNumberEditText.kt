package com.pechuro.cashdebts.ui.custom.phone

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import com.pechuro.cashdebts.R
import kotlinx.android.synthetic.main.layout_phone_edit_text.view.*

class PhoneNumberEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val textCode: EditText
    private val textNumber: HintEditText

    init {
        inflate(context, R.layout.layout_phone_edit_text, this)
        textCode = text_code
        textNumber = text_number
    }

    fun setCountryData(data: CountyData) {
        textCode.setText("+${data.phonePrefix}")
        textNumber.hintText = data.phonePattern
    }
}