package com.pechuro.cashdebts.ui.fragment.localdebtlist

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.repositories.ILocalDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebtDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalDebtListFragmentViewModel @Inject constructor(
    private val debtRepository: ILocalDebtRepository,
    private val userRepository: IUserRepository,
    private val diffCallback: LocalDebtDiffCallback
) : BaseViewModel() {

    private var previousDeletedDebt: LocalDebt? = null

    val debtSource = debtRepository.getSource()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .map { map ->
            map
                .toList()
                .map {
                    LocalDebt(
                        it.first,
                        it.second.name,
                        it.second.value,
                        it.second.description,
                        it.second.date,
                        it.second.role
                    )
                }
        }
        .scan { first: List<LocalDebt>, second: List<LocalDebt> ->
            val mergedList = first.filter { it in second } + second
            mergedList.toSet().toList()
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
        .filter { diffCallback.oldList != it }
        .map {
            diffCallback.newList = it
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffCallback.oldList = it
            DiffResult(diffResult, it)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .replay(1)

    init {
        debtSource.connect().addTo(compositeDisposable)
    }

    fun deleteDebt(debt: LocalDebt) {
        previousDeletedDebt = debt
        debtRepository.delete(debt.id).onErrorComplete().subscribe().addTo(compositeDisposable)
    }

    fun restoreDebt() {
        val debt = previousDeletedDebt ?: return
        debtRepository.update(debt.id, debt.firestoreDebt()).onErrorComplete().subscribe()
            .addTo(compositeDisposable)
    }

    private fun LocalDebt.firestoreDebt() = FirestoreLocalDebt(
        userRepository.currentUserBaseInformation.uid,
        personName,
        value,
        description,
        date,
        role
    )
}
