package com.pechuro.cashdebts.ui.fragment.localdebtlist

import com.pechuro.cashdebts.data.data.repositories.IDebtRepository
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class LocalDebtListFragmentViewModel @Inject constructor(
    private val debtRepository: IDebtRepository
) : BaseViewModel() {

    val debtSource = debtRepository.getLocalDebtSource()
        .map {
            LocalDebt(it.name, it.value, it.description, it.date)
        }
        .observeOn(AndroidSchedulers.mainThread())


}
