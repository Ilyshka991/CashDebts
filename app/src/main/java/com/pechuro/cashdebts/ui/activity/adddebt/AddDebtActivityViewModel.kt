package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.data.model.FirestoreDebt
import com.pechuro.cashdebts.data.repositories.IDebtRepository
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddDebtActivityViewModel @Inject constructor(
    private val debtRepository: IDebtRepository
) : BaseViewModel() {
    val debt = ObservableField<FirestoreDebt>(FirestoreDebt())
    val command = PublishSubject.create<Events>()

    fun setData(name: String, phoneNumber: String) {
        debt.get()?.apply {
            debtorName = name
            debtorPhone = phoneNumber.replace(Regex("[ -]"), "")
        }
        debt.notifyChange()
    }

    fun save() {
        debtRepository.add(debt.get()!!).subscribe {
            command.onNext(Events.OnSaved)
        }.let(compositeDisposable::add)
    }
}

sealed class Events {
    object OnSaved : Events()

    class ShowSnackBarError(@StringRes val id: Int) : Events()
}