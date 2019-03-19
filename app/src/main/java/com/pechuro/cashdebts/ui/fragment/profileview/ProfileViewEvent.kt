package com.pechuro.cashdebts.ui.fragment.profileview

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class ProfileViewEvent : BaseEvent() {
    object OnLogout : ProfileViewEvent()
    object OpenEditProfile : ProfileViewEvent()
}