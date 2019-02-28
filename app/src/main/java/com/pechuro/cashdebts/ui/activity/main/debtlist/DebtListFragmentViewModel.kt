package com.pechuro.cashdebts.ui.activity.main.debtlist

import com.google.firebase.firestore.DocumentSnapshot
import com.pechuro.cashdebts.data.CurrentUser
import com.pechuro.cashdebts.data.FirestoreRepository
import com.pechuro.cashdebts.data.FirestoreStructure.Debts.Structure.creditor
import com.pechuro.cashdebts.data.FirestoreStructure.Debts.Structure.debtor
import com.pechuro.cashdebts.data.FirestoreStructure.Debts.Structure.description
import com.pechuro.cashdebts.data.FirestoreStructure.Debts.Structure.value
import com.pechuro.cashdebts.ui.activity.main.debtlist.data.Debt
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DebtListFragmentViewModel @Inject constructor(
    repository: FirestoreRepository,
    private val user: CurrentUser
) : BaseViewModel() {

    val dataSource = repository.getDataSource()
        .map { it.type to it.document.toDebt() }
        .observeOn(AndroidSchedulers.mainThread())

    private fun DocumentSnapshot.toDebt(): Debt {
        val isCurrentUserCreditor = getString(creditor) == user.phoneNumber
        return Debt(
            id,
            if (isCurrentUserCreditor) getString(debtor)!! else getString(creditor)!!,
            getDouble(value)!!,
            getString(description)
        )
    }
}
