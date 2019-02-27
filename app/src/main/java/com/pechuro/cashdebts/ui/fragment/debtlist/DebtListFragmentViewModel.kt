package com.pechuro.cashdebts.ui.fragment.debtlist

import com.google.firebase.firestore.DocumentSnapshot
import com.pechuro.cashdebts.data.model.CurrentUser
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.data.remote.FirestoreRepository
import com.pechuro.cashdebts.data.remote.FirestoreStructure
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.debtee
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.debtor
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.description
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.value
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DebtListFragmentViewModel @Inject constructor(
    repository: FirestoreRepository,
    private val user: CurrentUser
) : BaseViewModel() {

    val dataSource = repository.getDataSource()
        .map { it.type to it.document.getDebt() }
        .observeOn(AndroidSchedulers.mainThread())

    fun add() {

    }

    private fun DocumentSnapshot.getDebt(): Debt {
        val isCurrentUserDebtor = getString(debtor) == user.phoneNumber
        return Debt(
            id,
            if (isCurrentUserDebtor) getString(debtee)!! else getString(FirestoreStructure.Debts.Structure.debtor)!!,
            getDouble(value)!!,
            getString(description)
        )
    }
}
