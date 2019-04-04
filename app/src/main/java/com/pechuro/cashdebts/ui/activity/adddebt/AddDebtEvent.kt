package com.pechuro.cashdebts.ui.activity.adddebt

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class AddDebtEvent : BaseEvent() {
    object OnSuccess : AddDebtEvent()
}