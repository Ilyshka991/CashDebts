package com.pechuro.cashdebts.ui.fragment.filterdialog

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class FilterEvent : BaseEvent() {
    object OnChange : FilterEvent()
}