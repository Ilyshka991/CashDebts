package com.pechuro.cashdebts.ui.fragment.remotedebtlist.data

import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus
import java.util.*

data class RemoteDebt(
    val id: String,
    val user: User,
    val value: Double,
    val description: String,
    val date: Date,
    @FirestoreDebtStatus val status: Int,
    @DebtRole val role: Int,
    var isExpanded: Boolean = false
) {
    fun isEmpty() = value == 0.0

    companion object {
        val EMPTY = RemoteDebt("", User(), 0.0, "", Date(), FirestoreDebtStatus.NOT_SEND, DebtRole.CREDITOR, false)
    }

    data class User(
        var firstName: String,
        var lastName: String,
        var phoneNumber: String,
        var photoUrl: String?
    ) {
        constructor() : this("", "", "", null)
    }
}