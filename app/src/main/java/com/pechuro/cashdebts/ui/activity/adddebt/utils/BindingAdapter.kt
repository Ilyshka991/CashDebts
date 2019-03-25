package com.pechuro.cashdebts.ui.activity.adddebt.utils

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.model.DebtRole
import com.pechuro.cashdebts.ui.custom.TextWatcher

@InverseBindingAdapter(attribute = "debt_role")
fun getIsPersonDebtor(view: ChipGroup) = when (view.checkedChipId) {
    R.id.chip_creditor -> DebtRole.CREDITOR
    R.id.chip_debtor -> DebtRole.DEBTOR
    else -> throw IllegalArgumentException()
}

@BindingAdapter("debt_role")
fun setIsPersonDebtor(view: ChipGroup, @DebtRole type: Int) {
    val checkedChipId = if (type == DebtRole.CREDITOR) R.id.chip_creditor else R.id.chip_debtor
    with(view) {
        check(checkedChipId)
        findViewById<Chip>(checkedChipId).isClickable = false
    }
}

@BindingAdapter("app:debt_roleAttrChanged")
fun setListeners(
    view: ChipGroup,
    attrChange: InverseBindingListener
) {
    val listener = ChipGroup.OnCheckedChangeListener { _, id ->
        for (i in 0 until view.childCount) {
            val chip = view.getChildAt(i)
            chip.isClickable = chip.id != id
        }
        attrChange.onChange()
    }
    view.setOnCheckedChangeListener(listener)
}


@InverseBindingAdapter(attribute = "android:text")
fun getValue(view: EditText) = if (view.text.isNullOrEmpty()) 0.0 else view.text.toString().toDouble()

@BindingAdapter("android:text")
fun setValue(view: EditText, value: Double) {
    view.setText(value.toString())
}

@BindingAdapter("app:android:textAttrChanged")
fun setValueListeners(
    view: EditText,
    attrChange: InverseBindingListener
) {
    val listener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            attrChange.onChange()
        }
    }
    view.addTextChangedListener(listener)
}