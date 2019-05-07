package com.pechuro.cashdebts.ui.activity.adddebt.model

import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.CREDITOR
import com.pechuro.cashdebts.ui.utils.extensions.requireValue
import io.reactivex.subjects.BehaviorSubject
import java.util.*

abstract class BaseDebtInfo {
    val value = BehaviorSubject.createDefault(0.0)
    val description = BehaviorSubject.createDefault("")
    val date = BehaviorSubject.createDefault(Date())
    @DebtRole
    val debtRole = BehaviorSubject.createDefault(CREDITOR)

    var id: String? = null

    abstract fun isValid(): Boolean

    fun isInfoValid() = value.requireValue > 0.0
}