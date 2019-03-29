package com.pechuro.cashdebts.ui.activity.adddebt.model.impl

import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo
import io.reactivex.subjects.BehaviorSubject

class LocalDebtInfo : BaseDebtInfo() {
    val name = BehaviorSubject.createDefault("")

    override fun isValid(): Boolean {
        return !name.value.isNullOrEmpty()
    }
}