package com.pechuro.cashdebts.ui.fragment.debtlist

import com.pechuro.cashdebts.data.repositories.IDebtRepository
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import javax.inject.Inject

class DebtListFragmentViewModel @Inject constructor(
    debtRepository: IDebtRepository
    /* private val user: UserBaseInformation*/
) : BaseViewModel() {

    /* val dataSource = debtRepository.getDataSource()
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
     }*/
}
