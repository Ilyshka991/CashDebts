package com.pechuro.cashdebts.ui.fragment.localdebtlist.data

import com.pechuro.cashdebts.data.data.model.DebtRole
import java.util.*

data class LocalDebt(
    val id: String,
    val personName: String,
    var value: Double,
    val description: String,
    val date: Date,
    @DebtRole var role: Int,
    var isExpanded: Boolean = false,
    var isUnited: Boolean = false
) {
    fun isEmpty() = personName.isEmpty() && value == 0.0

    companion object {
        val EMPTY = LocalDebt("", "", 0.0, "", Date(), DebtRole.CREDITOR, false)
    }
}