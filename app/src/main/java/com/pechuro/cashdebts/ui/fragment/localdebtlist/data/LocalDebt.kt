package com.pechuro.cashdebts.ui.fragment.localdebtlist.data

import com.pechuro.cashdebts.data.data.model.DebtRole
import java.util.*

data class LocalDebt(
    val personName: String,
    val value: Double,
    val description: String?,
    val date: Date,
    @DebtRole val role: Int,
    var isExpanded: Boolean = false
) {
    fun isEmpty() = personName.isEmpty() && value == 0.0

    companion object {
        val EMPTY = LocalDebt("", 0.0, null, Date(), DebtRole.CREDITOR)
    }
}