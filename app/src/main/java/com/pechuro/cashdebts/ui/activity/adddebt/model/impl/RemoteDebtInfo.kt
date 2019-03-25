package com.pechuro.cashdebts.ui.activity.adddebt.model.impl

import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo

class RemoteDebtInfo(
    var phone: String,
    var personUid: String
) : BaseDebtInfo() {
    constructor() : this("", "")

    override fun isValid(): Boolean {
        return phone.isNotBlank()
    }
}