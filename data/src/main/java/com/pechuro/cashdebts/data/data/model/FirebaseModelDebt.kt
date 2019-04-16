package com.pechuro.cashdebts.data.data.model

import androidx.annotation.IntDef
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.CREDITOR
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETE
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.CONFIRMATION_APPROVED
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.CONFIRMATION_REJECTED
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.NOT_SEND
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETION_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETION_FROM_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_CONFIRMATION
import java.util.*

abstract class FirestoreBaseDebt(
    val value: Double,
    val description: String,
    val date: Date
)

class FirestoreRemoteDebt(
    val creditorUid: String,
    val debtorUid: String,
    value: Double,
    description: String,
    date: Date,
    @FirestoreDebtStatus val status: Int
) : FirestoreBaseDebt(value, description, date) {
    constructor() : this("", "", 0.0, "", Date(), FirestoreDebtStatus.NOT_SEND)
}

class FirestoreLocalDebt(
    val ownerUid: String,
    val name: String,
    value: Double,
    description: String,
    date: Date,
    @DebtRole val role: Int
) : FirestoreBaseDebt(value, description, date) {
    constructor() : this("", "", 0.0, "", Date(), DebtRole.CREDITOR)
}

@IntDef(
    NOT_SEND,
    WAIT_FOR_CONFIRMATION, CONFIRMATION_REJECTED,
    CONFIRMATION_APPROVED, WAIT_FOR_COMPLETION_FROM_CREDITOR,
    WAIT_FOR_COMPLETION_FROM_DEBTOR, COMPLETION_REJECTED_BY_CREDITOR,
    COMPLETION_REJECTED_BY_DEBTOR, COMPLETE
)
@Retention(AnnotationRetention.SOURCE)
annotation class FirestoreDebtStatus {
    companion object {
        const val NOT_SEND = 0
        const val WAIT_FOR_CONFIRMATION = 1
        const val CONFIRMATION_REJECTED = 2
        const val CONFIRMATION_APPROVED = 3
        const val WAIT_FOR_COMPLETION_FROM_CREDITOR = 4
        const val WAIT_FOR_COMPLETION_FROM_DEBTOR = 5
        const val COMPLETION_REJECTED_BY_CREDITOR = 6
        const val COMPLETION_REJECTED_BY_DEBTOR = 7
        const val COMPLETE = 8
    }
}

@IntDef(CREDITOR, DEBTOR)
@Retention(AnnotationRetention.SOURCE)
annotation class DebtRole {
    companion object {
        const val CREDITOR = 1
        const val DEBTOR = 2
    }
}