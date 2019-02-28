package com.pechuro.cashdebts.ui.activity.main

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class MainActivityEvent : BaseEvent() {
    object OpenAddActivity : MainActivityEvent()
}