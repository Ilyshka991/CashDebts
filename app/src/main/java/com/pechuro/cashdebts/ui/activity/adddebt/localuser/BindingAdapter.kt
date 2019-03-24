package com.pechuro.cashdebts.ui.activity.adddebt.localuser

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.pechuro.cashdebts.R

@InverseBindingAdapter(attribute = "is_person_debtor")
fun getIsPersonDebtor(view: ChipGroup) = when (view.checkedChipId) {
    R.id.chip_creditor -> false
    R.id.chip_debtor -> true
    else -> throw IllegalArgumentException()
}

@BindingAdapter("is_person_debtor")
fun setIsPersonDebtor(view: ChipGroup, isDebtor: Boolean) {
    val checkedChipId = if (isDebtor) R.id.chip_debtor else R.id.chip_creditor
    with(view) {
        check(checkedChipId)
        findViewById<Chip>(checkedChipId).isClickable = false
    }
}

@BindingAdapter("app:is_person_debtorAttrChanged")
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