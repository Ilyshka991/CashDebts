package com.pechuro.cashdebts.ui.activity.adddebt.model

import androidx.databinding.BaseObservable
import java.util.*

abstract class BaseDebtInfo(
    var value: Double,
    var description: String?,
    var date: Date
) : BaseObservable() {
    constructor() : this(0.0, null, Date())
}