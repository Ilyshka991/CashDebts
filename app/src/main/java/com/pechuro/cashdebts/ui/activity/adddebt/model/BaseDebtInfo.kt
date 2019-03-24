package com.pechuro.cashdebts.ui.activity.adddebt.model

import androidx.annotation.IntDef
import androidx.databinding.BaseObservable
import com.pechuro.cashdebts.ui.activity.adddebt.model.DebtRole.Companion.CREDITOR
import com.pechuro.cashdebts.ui.activity.adddebt.model.DebtRole.Companion.DEBTOR
import java.util.*

abstract class BaseDebtInfo(
    var value: Double,
    var description: String?,
    var date: Date,
    @DebtRole var debtRole: Int
) : BaseObservable() {
    constructor() : this(0.0, null, Date(), CREDITOR)
}

@IntDef(CREDITOR, DEBTOR)
@Retention(AnnotationRetention.SOURCE)
annotation class DebtRole {
    companion object {
        const val CREDITOR = 1
        const val DEBTOR = 2
    }
}