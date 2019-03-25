package com.pechuro.cashdebts.ui.activity.adddebt.model

import androidx.databinding.BaseObservable
import com.pechuro.cashdebts.data.model.DebtRole
import com.pechuro.cashdebts.data.model.DebtRole.Companion.CREDITOR
import java.util.*

abstract class BaseDebtInfo(
    var value: Double,
    var description: String?,
    var date: Date,
    @DebtRole var debtRole: Int
) : BaseObservable() {
    constructor() : this(0.0, null, Date(), CREDITOR)

    open fun isValid(): Boolean {
        return true
    }
}