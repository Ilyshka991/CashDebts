package com.pechuro.cashdebts.ui.fragment.datetimepicker

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseDialog
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.dialog_datetime_picker.*
import java.util.*

class DateTimePickerDialog : BaseDialog<DateTimePickerDialogViewModel>() {
    override val layoutId: Int
        get() = R.layout.dialog_datetime_picker

    private var isDateSet = false
        set(value) {
            field = value
            picker_date.isInvisible = value
            picker_time.isInvisible = !value
        }

    override fun getViewModelClass() = DateTimePickerDialogViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
        val isDateSet = savedInstanceState?.getBoolean(BUNDLE_IS_DATE_SET) ?: false
        this.isDateSet = isDateSet
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BUNDLE_IS_DATE_SET, isDateSet)
    }

    private fun setViewListeners() {
        button_ok.setOnClickListener {
            if (isDateSet) {
                finish()
            } else {
                isDateSet = true
            }
        }
        button_cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setupView() {
        picker_time.apply {
            setIs24HourView(true)
        }
        picker_date.apply {
            maxDate = Date().time
        }
    }

    private fun finish() {
        val date = Calendar.getInstance().apply {
            set(
                picker_date.year,
                picker_date.month,
                picker_date.dayOfMonth,
                picker_time.currentHour,
                picker_time.currentMinute
            )
        }.let { it.time }
        EventBus.publish(DateTimePickerEvent.OnDateSelected(date))
        dismiss()
    }

    companion object {
        const val TAG = "DateTimePickerDialog"

        private const val BUNDLE_IS_DATE_SET = "isDateSet"

        fun newInstance() = DateTimePickerDialog()
    }
}