package com.pechuro.cashdebts.ui.activity.adddebt.utils

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.model.DebtRole

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