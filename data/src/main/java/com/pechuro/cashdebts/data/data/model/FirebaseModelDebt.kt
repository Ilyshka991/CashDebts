package com.pechuro.cashdebts.data.data.model

import androidx.annotation.IntDef
import androidx.annotation.Keep
import com.pechuro.cashdebts.data.data.model.DebtDeleteStatus.Companion.CACHED
import com.pechuro.cashdebts.data.data.model.DebtDeleteStatus.Companion.DELETED_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.DebtDeleteStatus.Companion.DELETED_FROM_DEBTOR
import com.pechuro.cashdebts.data.data.model.DebtDeleteStatus.Companion.NOT_DELETED
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.CREDITOR
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETE
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.CONFIRMATION_REJECTED
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.EDIT_CONFIRMATION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.EDIT_CONFIRMATION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.IN_PROGRESS
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.NOT_SEND
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETE_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETE_FROM_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_CONFIRMATION
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR
import java.util.*

@Keep
abstract class FirestoreBaseDebt(
    val value: Double,
    val description: String,
    val date: Date
)

@Keep
class FirestoreRemoteDebt(
    val creditorUid: String,
    val debtorUid: String,
    value: Double,
    description: String,
    date: Date,
    @FirestoreDebtStatus val status: Int,
    val initPersonUid: String,
    @DebtDeleteStatus val deleteStatus: Int
) : FirestoreBaseDebt(value, description, date)

@Keep
class FirestoreLocalDebt(
    val ownerUid: String,
    val name: String,
    value: Double,
    description: String,
    date: Date,
    @DebtRole val role: Int
) : FirestoreBaseDebt(value, description, date)

@IntDef(
    NOT_SEND,
    WAIT_FOR_CONFIRMATION, CONFIRMATION_REJECTED,
    IN_PROGRESS, WAIT_FOR_COMPLETE_FROM_CREDITOR,
    WAIT_FOR_COMPLETE_FROM_DEBTOR, COMPLETION_REJECTED_BY_CREDITOR,
    COMPLETION_REJECTED_BY_DEBTOR, COMPLETE,
    WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR, WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR,
    EDIT_CONFIRMATION_REJECTED_BY_CREDITOR, EDIT_CONFIRMATION_REJECTED_BY_DEBTOR
)
@Retention(AnnotationRetention.SOURCE)
annotation class FirestoreDebtStatus {
    companion object {
        const val NOT_SEND = 0
        const val WAIT_FOR_CONFIRMATION = 1
        const val CONFIRMATION_REJECTED = 2
        const val IN_PROGRESS = 3
        const val WAIT_FOR_COMPLETE_FROM_CREDITOR = 4
        const val WAIT_FOR_COMPLETE_FROM_DEBTOR = 5
        const val COMPLETION_REJECTED_BY_CREDITOR = 6
        const val COMPLETION_REJECTED_BY_DEBTOR = 7
        const val COMPLETE = 8
        const val WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR = 9
        const val WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR = 10
        const val EDIT_CONFIRMATION_REJECTED_BY_CREDITOR = 11
        const val EDIT_CONFIRMATION_REJECTED_BY_DEBTOR = 12
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

@IntDef(NOT_DELETED, DELETED_FROM_CREDITOR, DELETED_FROM_DEBTOR, CACHED)
@Retention(AnnotationRetention.SOURCE)
annotation class DebtDeleteStatus {
    companion object {
        const val NOT_DELETED = 0
        const val DELETED_FROM_CREDITOR = 1
        const val DELETED_FROM_DEBTOR = 2
        const val CACHED = 3
    }
}