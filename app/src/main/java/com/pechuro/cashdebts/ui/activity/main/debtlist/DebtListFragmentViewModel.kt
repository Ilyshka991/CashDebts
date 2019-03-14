package com.pechuro.cashdebts.ui.activity.main.debtlist

import com.google.firebase.firestore.DocumentSnapshot
import com.pechuro.cashdebts.data.repositories.FirestoreDebtRepository
import com.pechuro.cashdebts.data.structure.FirestoreStructure.Debts.Structure.creditor
import com.pechuro.cashdebts.data.structure.FirestoreStructure.Debts.Structure.debtor
import com.pechuro.cashdebts.data.structure.FirestoreStructure.Debts.Structure.description
import com.pechuro.cashdebts.data.structure.FirestoreStructure.Debts.Structure.value
import com.pechuro.cashdebts.ui.activity.main.debtlist.data.Debt
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DebtListFragmentViewModel @Inject constructor(
    debtRepository: FirestoreDebtRepository
    /* private val user: CurrentUser*/
) : BaseViewModel() {

    val dataSource = debtRepository.getDataSource()
        .map { it.type to it.document.toDebt() }
        .observeOn(AndroidSchedulers.mainThread())

    private fun DocumentSnapshot.toDebt(): Debt {
        val isCurrentUserCreditor = getString(creditor) == "Ff"
        return Debt(
            id,
            if (isCurrentUserCreditor) getString(debtor)!! else getString(creditor)!!,
            getDouble(value)!!,
            getString(description)
        )
    }
}
