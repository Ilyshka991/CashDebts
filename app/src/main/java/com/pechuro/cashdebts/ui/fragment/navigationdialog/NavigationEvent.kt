package com.pechuro.cashdebts.ui.fragment.navigationdialog

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class NavigationEvent : BaseEvent() {
    object OpenRemoteDebts : NavigationEvent()
    object OpenLocalDebts : NavigationEvent()
    object OpenProfile : NavigationEvent()
}