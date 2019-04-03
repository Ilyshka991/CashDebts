package com.pechuro.cashdebts.ui.fragment.datetimepicker

import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_datetime_picker.*

class DateTimePickerDialog : BaseDialog<DateTimePickerDialogViewModel>() {
    override val layoutId: Int
        get() = R.layout.dialog_datetime_picker

    override fun getViewModelClass() = DateTimePickerDialogViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {
        picker_date.init(2019, 1, 2) { view, year, month, day ->
            println("AAAAAAAAAAA")
            picker_time.isVisible = true
            picker_date.isInvisible = true
        }
        picker_time.setOnTimeChangedListener { view, hourOfDay, minute ->
            println("BBBBBBBBBBBB")
        }

    }

    companion object {
        const val TAG = "DateTimePickerDialog"

        fun newInstance() = DateTimePickerDialog()
    }
}