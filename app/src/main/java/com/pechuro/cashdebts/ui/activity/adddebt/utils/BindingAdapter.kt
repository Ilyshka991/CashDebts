package com.pechuro.cashdebts.ui.activity.adddebt.utils

/*

object BindingAdapter {

    object DebtRoleAdapter {
        @JvmStatic
        @InverseBindingAdapter(attribute = "debt_role")
        fun getIsPersonDebtor(view: ChipGroup) = when (view.checkedChipId) {
            R.id.chip_creditor -> DebtRole.CREDITOR
            R.id.chip_debtor -> DebtRole.DEBTOR
            else -> throw IllegalArgumentException()
        }

        @JvmStatic
        @BindingAdapter("debt_role")
        fun setIsPersonDebtor(view: ChipGroup, @DebtRole type: Int) {
            val checkedChipId = if (type == DebtRole.CREDITOR) R.id.chip_creditor else R.id.chip_debtor
            with(view) {
                check(checkedChipId)
                findViewById<Chip>(checkedChipId).isClickable = false
            }
        }

        @JvmStatic
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
    }

    object EditTextDoubleAdapter {
        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun getValue(view: EditText) = if (view.text.isNullOrEmpty()) 0.0 else view.text.toString().toDouble()

        @JvmStatic
        @BindingAdapter("android:text")
        fun setValue(view: EditText, value: Double) {
            view.setText(value.toString())
        }

        @JvmStatic
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
    }


    @JvmStatic
    @BindingAdapter("date")
    fun setDate(view: EditText, date: Date) {
        val dateFormatter = SimpleDateFormat("")
    }

}*/
