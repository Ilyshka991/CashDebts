package com.pechuro.cashdebts.ui.fragment.localdebtlist

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.data.data.repositories.IDebtRepository
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebtDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalDebtListFragmentViewModel @Inject constructor(
    private val debtRepository: IDebtRepository,
    private val diffCallback: LocalDebtDiffCallback
) : BaseViewModel() {

    val debtSource = debtRepository.getLocalDebtSource()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .map {
            it.map { LocalDebt(it.name, it.value, it.description, it.date, it.role) }
        }
        .map {
            val resultList = mutableListOf<LocalDebt>()
            if (it.isEmpty()) {
                resultList += LocalDebt.EMPTY
            } else {
                resultList += it
            }
            resultList
        }
        .map { list ->
            list.sortedByDescending { it.date }
        }
        .map {
            diffCallback.newList = it
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffCallback.oldList = it
            DiffResult(diffResult, it)
        }
        .replay(1)
        .autoConnect()
        .observeOn(AndroidSchedulers.mainThread())
}
