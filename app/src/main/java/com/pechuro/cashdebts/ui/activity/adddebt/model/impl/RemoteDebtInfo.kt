package com.pechuro.cashdebts.ui.activity.adddebt.model.impl

import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo
import io.reactivex.subjects.BehaviorSubject

class RemoteDebtInfo : BaseDebtInfo() {
    val phone = BehaviorSubject.createDefault("")
    val personUid = BehaviorSubject.createDefault("")
    val isPersonChangeEnabled = BehaviorSubject.createDefault(true)

    override fun isValid(): Boolean {
        return !phone.value.isNullOrEmpty()
    }
}