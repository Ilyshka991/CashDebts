package com.pechuro.cashdebts.ui.activity.adddebt.model.impl

import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo

class RemoteDebtInfo(
    var creditorPhone: String,
    var creditorName: String,
    var debtorPhone: String,
    var debtorName: String
) : BaseDebtInfo() {
    constructor() : this("", "", "", "")
}