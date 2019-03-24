package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.LocalDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.RemoteDebtInfo
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddDebtActivityViewModel @Inject constructor(
    private val debtRepository: IDebtRepository,
    private val userRepository: IUserRepository
) : BaseViewModel() {
    val debt = ObservableField<BaseDebtInfo>()
    val command = PublishSubject.create<Events>()

    fun setInitialData(isLocalDebt: Boolean) {
        if (debt.get() == null) {
            debt.set(if (isLocalDebt) LocalDebtInfo() else RemoteDebtInfo())
        }
    }

    fun setPhoneData(phoneNumber: String) {
        (debt.get() as RemoteDebtInfo).phone = phoneNumber.replace(Regex("[ -]"), "")
        debt.notifyChange()
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
            is RemoteDebtInfo -> {
                if (data.isValid()) {
                    checkUserExist(data.phone)
                } else {
                    command.onNext(Events.ShowSnackBarError(R.string.add_debt_error_invalid_phone))
                }
            }
        }
    }

    private fun checkUserExist(phone: String) {
        command.onNext(Events.ShowProgress)
        userRepository.isUserWithPhoneNumberExist(phone).subscribe({
            command.onNext(Events.DismissProgress)
            command.onNext(if (it) Events.OpenInfo else Events.ShowSnackBarUserNotExist)
        }, {
            it.printStackTrace()
            command.onNext(Events.DismissProgress)
            command.onNext(Events.ShowSnackBarError(R.string.add_debt_error_common))
        }).addTo(compositeDisposable)
    }

    sealed class Events {
        object OnSaved : Events()
        object OpenInfo : Events()
        object ShowProgress : Events()
        object DismissProgress : Events()
        class ShowSnackBarError(@StringRes val id: Int) : Events()
        object ShowSnackBarUserNotExist : Events()
    }
}