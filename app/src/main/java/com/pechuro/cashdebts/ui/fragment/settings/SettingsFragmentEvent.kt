package com.pechuro.cashdebts.ui.fragment.settings

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class SettingsFragmentEvent : BaseEvent() {
    object OnApplyChanges : SettingsFragmentEvent()
}