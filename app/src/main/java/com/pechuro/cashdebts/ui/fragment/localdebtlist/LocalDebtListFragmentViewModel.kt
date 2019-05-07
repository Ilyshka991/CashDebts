package com.pechuro.cashdebts.ui.fragment.localdebtlist

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.repositories.ILocalDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.entity.DiffResult
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebtDiffCallback
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebtsUiInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalDebtListFragmentViewModel @Inject constructor(
    private val debtRepository: ILocalDebtRepository,
    private val userRepository: IUserRepository,
    private val diffCallback: LocalDebtDiffCallback,
    private val prefsManager: PrefsManager
) : BaseViewModel() {

    private var previousDeletedDebt: LocalDebt? = null

    val debtSource: ConnectableObservable<LocalDebtsUiInfo> = debtRepository.getSource()
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
        .map { debts ->
            if (prefsManager.filterUnitePersons) {
                val singleItems = debts
                    .groupBy { it.personName }
                    .filter { it.value.size == 1 }
                    .toList()
                        .map { it.second[0] }
                val groupedItems = (debts - singleItems)
                    .groupingBy { it.personName }
                    .aggregate { _: String, accumulator: LocalDebt?, element: LocalDebt, first: Boolean ->
                        if (first) {
                            element.apply {
                                if (role == DebtRole.DEBTOR) {
                                    value = -value
                                }
                                isUnited = true
                            }
                        } else {
                            accumulator!!.apply {
                                value += if (element.role == DebtRole.CREDITOR) element.value else -element.value
                            }
                        }
                    }
                    .toList().map {
                        it.second!!.apply {
                            if (value < 0) {
                                value = -value
                                role = DebtRole.DEBTOR
                            } else {
                                role = DebtRole.CREDITOR
                            }
                        }
                    }
                groupedItems + singleItems
            } else {
                debts
            }
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
        .map {
            val totalValue = it.dataList
                .fold(0.0) { acc, debt ->
                    acc + if (debt.role == DebtRole.CREDITOR) debt.value else -debt.value
                }
            LocalDebtsUiInfo(it, totalValue)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .replay(1)

    private var debtSourceConnection: Disposable? = null

    init {
        initSource()
    }

    fun initSource() {
        debtSourceConnection?.dispose()
        debtSourceConnection = debtSource.connect()
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
