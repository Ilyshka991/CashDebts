package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.repositories.IDebtRepository
import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.LocalDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.RemoteDebtInfo
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddDebtActivityViewModel @Inject constructor(
    private val debtRepository: IDebtRepository
) : BaseViewModel() {
    val debt = ObservableField<BaseDebtInfo>()
    val command = PublishSubject.create<Events>()

    fun setInitialData(isLocalDebt: Boolean) {
        if (debt.get() == null) {
            debt.set(if (isLocalDebt) LocalDebtInfo() else RemoteDebtInfo())
        }
    }

    fun setData(name: String, phoneNumber: String) {
        /* debt.get()?.apply {
             debtorName = name
             debtorPhone = phoneNumber.replace(Regex("[ -]"), "")
         }
         debt.notifyChange()*/
    }

    fun save() {
        /*   debtRepository.add(debt.get()!!).subscribe {
               command.onNext(Events.OnSaved)
           }.let(compositeDisposable::add)*/
    }

    fun openInfo() {
        when (val data = debt.get()) {
            is LocalDebtInfo -> {
                if (data.isValid()) {
                    command.onNext(Events.OpenInfo)
                } else {
                    command.onNext(Events.ShowSnackBarError(R.string.add_debt_error_invalid_name))
                }
            }
        }

    }

    sealed class Events {
        object OnSaved : Events()
        object OpenInfo : Events()
        class ShowSnackBarError(@StringRes val id: Int) : Events()
    }
}