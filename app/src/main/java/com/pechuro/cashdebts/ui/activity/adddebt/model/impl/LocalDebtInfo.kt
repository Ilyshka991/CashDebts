package com.pechuro.cashdebts.ui.activity.adddebt.model.impl

import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo

class LocalDebtInfo(
    var name: String,
    var ownerDebtor: Boolean
) : BaseDebtInfo() {
    constructor() : this("", false)

    fun isValid(): Boolean {
        return name.isNotBlank()
    }
}