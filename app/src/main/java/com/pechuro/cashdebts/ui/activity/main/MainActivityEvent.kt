package com.pechuro.cashdebts.ui.activity.main

import androidx.annotation.StringRes
import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class MainActivityEvent : BaseEvent() {
    class OpenAddActivity(val isLocalDebt: Boolean, val id: String? = null) : MainActivityEvent()

    class ShowSnackbar(@StringRes val msgId: Int)
}
