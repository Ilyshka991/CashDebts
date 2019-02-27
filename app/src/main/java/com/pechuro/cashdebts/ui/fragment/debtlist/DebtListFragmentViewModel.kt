package com.pechuro.cashdebts.ui.fragment.debtlist

import com.pechuro.cashdebts.data.local.database.dao.DebtDao
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DebtListFragmentViewModel @Inject constructor(private val dao: DebtDao) : BaseViewModel() {
    val dataList = BehaviorSubject.create<List<Debt>>()

    init {
        getData()

    }

    fun add() {}

    private fun getData() {
        dao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                dataList.onNext(it)
            }
            .let(compositeDisposable::add)
    }
}
