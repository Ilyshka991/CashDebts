package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.data.data.repositories.IRemoteDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebtDiffCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RemoteDebtListFragmentViewModel @Inject constructor(
    private val debtRepository: IRemoteDebtRepository,
    private val userRepository: IUserRepository,
    private val diffCallback: RemoteDebtDiffCallback
) : BaseViewModel() {

    val debtSource = debtRepository.getSource()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .map { map ->
            map.toList()
                .map {
                    RemoteDebt(
                        it.first,
                        it.second.value,
                        it.second.description,
                        it.second.date
                    )
                }
        }
        .scan { first: List<RemoteDebt>, second: List<RemoteDebt> ->
            val mergedList = first.filter { it in second } + second
            mergedList.toSet().toList()
        }
        .map {
            val resultList = mutableListOf<RemoteDebt>()
            if (it.isEmpty()) {
                resultList += RemoteDebt.EMPTY
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
}