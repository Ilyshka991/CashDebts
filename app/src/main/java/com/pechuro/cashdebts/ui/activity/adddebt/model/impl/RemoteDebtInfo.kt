package com.pechuro.cashdebts.ui.activity.adddebt.model.impl

import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo

class RemoteDebtInfo(
    var phone: String
) : BaseDebtInfo() {
    constructor() : this("")

    fun isValid(): Boolean {
        return phone.isNotBlank()
    }
}