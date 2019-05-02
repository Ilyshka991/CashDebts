package com.pechuro.cashdebts.ui.fragment.navigationdialog

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class NavigationEvent : BaseEvent() {
    object openRemoteDebts : NavigationEvent()
    object openLocalDebts : NavigationEvent()
    object openProfile : NavigationEvent()
}