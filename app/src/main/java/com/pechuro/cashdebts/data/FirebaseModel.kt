package com.pechuro.cashdebts.data

import androidx.annotation.IntDef
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.COMPLETE
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.CONFIRMATION_APPROVED
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.CONFIRMATION_REJECTED
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.NOT_SEND
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETION_FROM_CREDITOR
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETION_FROM_DEBTOR
import com.pechuro.cashdebts.data.FirestoreDebtStatus.Companion.WAIT_FOR_CONFIRMATION
import java.util.*

data class FirestoreDebt(
    val creditor: String,
    val debtor: String,
    val value: Double,
    val description: String?,
    val date: Date,
    @FirestoreDebtStatus val status: Int
)

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
        const val NOT_SEND = 1
        const val WAIT_FOR_CONFIRMATION = 2
        const val CONFIRMATION_REJECTED = 3
        const val CONFIRMATION_APPROVED = 4
        const val WAIT_FOR_COMPLETION_FROM_CREDITOR = 5
        const val WAIT_FOR_COMPLETION_FROM_DEBTOR = 6
        const val COMPLETION_REJECTED_BY_CREDITOR = 7
        const val COMPLETION_REJECTED_BY_DEBTOR = 8
        const val COMPLETE = 9
    }
}