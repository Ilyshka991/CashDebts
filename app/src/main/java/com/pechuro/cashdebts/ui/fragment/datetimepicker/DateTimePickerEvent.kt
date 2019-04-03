package com.pechuro.cashdebts.ui.fragment.datetimepicker

import com.pechuro.cashdebts.ui.utils.BaseEvent
import java.util.*

sealed class DateTimePickerEvent : BaseEvent() {
    class OnDateSelected(val date: Date) : DateTimePickerEvent()
}