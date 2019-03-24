package com.pechuro.cashdebts.ui.activity.adddebt.model.impl

import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo

class LocalDebtInfo(
    var name: String
) : BaseDebtInfo() {
    constructor() : this("")

    fun isValid(): Boolean {
        return name.isNotBlank()
    }
}