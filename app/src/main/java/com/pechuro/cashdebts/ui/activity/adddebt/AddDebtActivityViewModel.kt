package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.data.CurrentUser
import com.pechuro.cashdebts.data.FirestoreDebt
import com.pechuro.cashdebts.data.FirestoreRepository
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddDebtActivityViewModel @Inject constructor(
    private val repository: FirestoreRepository,
    private val user: CurrentUser
) : BaseViewModel() {
    val debt = ObservableField<FirestoreDebt>(FirestoreDebt())
    val command = PublishSubject.create<Events>()

    init {
        println("AAAAAAAAAAAAA ${user.displayName}")
    }

    fun setData(name: String, phoneNumber: String) {
        debt.get()?.apply {
            debtorName = name
            debtorPhone = phoneNumber.replace(Regex("[ -]"), "")
        }
        debt.notifyChange()
    }

    fun save() {
        repository.add(debt.get()!!).subscribe {
            command.onNext(Events.OnSaved)
        }.let(compositeDisposable::add)
    }
}

sealed class Events {
    object OnSaved : Events()

    class ShowSnackBarError(@StringRes val id: Int) : Events()
}