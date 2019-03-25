package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.exception.FirestoreUserNotFoundException
import com.pechuro.cashdebts.data.model.DebtRole.Companion.CREDITOR
import com.pechuro.cashdebts.data.model.DebtRole.Companion.DEBTOR
import com.pechuro.cashdebts.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.model.FirestoreRemoteDebt
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
                    checkUserExist(data)
                } else {
                    command.onNext(Events.ShowSnackBarError(R.string.add_debt_error_invalid_phone))
                }
            }
        }
    }

    fun save() {
        command.onNext(Events.ShowProgress)
        val debt = debt.get()
        when {
            debt == null -> command.onNext(Events.ShowSnackBarError(R.string.add_debt_error_common))
            !debt.isValid() -> command.onNext(Events.ShowSnackBarError(R.string.add_debt_error_invalid_info))
            debt is LocalDebtInfo -> addLocalDebt(debt)
            debt is RemoteDebtInfo -> addRemoteDebt(debt)
        }
    }

    private fun addRemoteDebt(debt: RemoteDebtInfo) {
        val creditorUid: String
        val debtorUid: String
        when (debt.debtRole) {
            CREDITOR -> {
                creditorUid = userRepository.currentUserBaseInformation.uid
                debtorUid = debt.personUid
            }
            DEBTOR -> {
                debtorUid = userRepository.currentUserBaseInformation.uid
                creditorUid = debt.personUid
            }
            else -> throw IllegalArgumentException()
        }

        val sendingDebt = FirestoreRemoteDebt(
            creditorUid,
            debtorUid,
            debt.value,
            debt.description,
            debt.date,
            FirestoreDebtStatus.NOT_SEND
        )

        debtRepository.add(sendingDebt).subscribe({
            command.onNext(Events.DismissProgress)
            command.onNext(Events.OnSaved)
        }, {
            command.onNext(Events.DismissProgress)
        }).addTo(compositeDisposable)
    }

    private fun addLocalDebt(debt: LocalDebtInfo) {
        val sendingDebt = FirestoreLocalDebt(
            userRepository.currentUserBaseInformation.uid,
            debt.name,
            debt.value,
            debt.description,
            debt.date,
            debt.debtRole
        )
        debtRepository.add(sendingDebt).subscribe({
            command.onNext(Events.DismissProgress)
            command.onNext(Events.OnSaved)
        }, {
            command.onNext(Events.DismissProgress)
        }).addTo(compositeDisposable)

    }

    private fun checkUserExist(data: RemoteDebtInfo) {
        command.onNext(Events.ShowProgress)
        userRepository.getUidByPhone(data.phone).subscribe({
            data.personUid = it
            command.onNext(Events.DismissProgress)
            command.onNext(Events.OpenInfo)
        }, {
            it.printStackTrace()
            command.onNext(Events.DismissProgress)
            when (it) {
                is FirestoreUserNotFoundException -> command.onNext(Events.ShowSnackBarUserNotExist)
                else -> command.onNext(Events.ShowSnackBarError(R.string.add_debt_error_common))
            }
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