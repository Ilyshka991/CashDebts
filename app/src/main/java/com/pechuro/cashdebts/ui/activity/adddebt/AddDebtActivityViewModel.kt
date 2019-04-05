package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.CREDITOR
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.DEBTOR
import com.pechuro.cashdebts.data.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.connectivity.ConnectivityListener
import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.LocalDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.RemoteDebtInfo
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.requireValue
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddDebtActivityViewModel @Inject constructor(
    private val debtRepository: IDebtRepository,
    private val userRepository: IUserRepository,
    private val connectivityListener: ConnectivityListener
) : BaseViewModel() {
    val command = PublishSubject.create<Events>()
    val isConnectionAvailable = BehaviorSubject.create<Boolean>()

    lateinit var debt: BaseDebtInfo

    init {
        setConnectivityListener()
    }

    fun setInitialData(isLocalDebt: Boolean) {
        if (!::debt.isInitialized) {
            debt = if (isLocalDebt) LocalDebtInfo() else RemoteDebtInfo()
        }
    }

    fun setPhoneData(phoneNumber: String) {
        (debt as RemoteDebtInfo).phone.onNext(phoneNumber)
    }

    fun openInfo() {
        when (val data = debt) {
            is LocalDebtInfo -> {
                if (data.isValid()) {
                    command.onNext(Events.OpenInfo(false))
                } else {
                    command.onNext(Events.OnError(R.string.add_debt_error_invalid_name))
                }
            }
            is RemoteDebtInfo -> {
                if (data.isValid()) {
                    checkUserExist(data)
                } else {
                    command.onNext(Events.OnError(R.string.add_debt_error_invalid_phone))
                }
            }
        }
    }

    fun save() {
        if (!debt.isInfoValid()) {
            command.onNext(Events.OnError(R.string.add_debt_error_invalid_info))
            return
        }
        command.onNext(Events.ShowProgress)
        when (val debt = debt) {
            is LocalDebtInfo -> addLocalDebt(debt)
            is RemoteDebtInfo -> addRemoteDebt(debt)
        }
    }

    fun restartWithLocalDebtFragment() {
        command.onNext(AddDebtActivityViewModel.Events.RestartWithLocalDebtFragment)
    }

    private fun setConnectivityListener() {
        connectivityListener.listen(isConnectionAvailable)
    }

    private fun addRemoteDebt(debt: RemoteDebtInfo) {
        val creditorUid: String
        val debtorUid: String
        when (debt.debtRole.value) {
            null -> {
                command.onNext(Events.OnError(R.string.add_debt_error_common))
                return
            }
            CREDITOR -> {
                creditorUid = userRepository.currentUserBaseInformation.uid
                debtorUid = debt.personUid.requireValue
            }
            DEBTOR -> {
                debtorUid = userRepository.currentUserBaseInformation.uid
                creditorUid = debt.personUid.requireValue
            }
            else -> throw IllegalArgumentException()
        }

        val sendingDebt = com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt(
            creditorUid,
            debtorUid,
            debt.value.requireValue,
            debt.description.requireValue,
            debt.date.requireValue,
            com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.NOT_SEND
        )

        debtRepository.add(sendingDebt).subscribe({
            command.onNext(Events.DismissProgress)
            command.onNext(Events.OnSaved)
        }, {
            command.onNext(Events.DismissProgress)
        }).addTo(compositeDisposable)
    }

    private fun addLocalDebt(debt: LocalDebtInfo) {
        val sendingDebt = com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt(
            userRepository.currentUserBaseInformation.uid,
            debt.name.requireValue,
            debt.value.requireValue,
            debt.description.requireValue,
            debt.date.requireValue,
            debt.debtRole.requireValue
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
        userRepository.getUidByPhone(data.phone.requireValue).subscribe({
            data.personUid.onNext(it)
            command.onNext(Events.DismissProgress)
            command.onNext(Events.OpenInfo(true))
        }, {
            it.printStackTrace()
            command.onNext(Events.DismissProgress)
            when (it) {
                is com.pechuro.cashdebts.data.data.exception.FirestoreUserNotFoundException -> command.onNext(Events.OnErrorUserNotExist)
                else -> command.onNext(Events.OnError(R.string.add_debt_error_common))
            }
        }).addTo(compositeDisposable)
    }

    sealed class Events {
        object OnSaved : Events()
        class OpenInfo(val isInternetRequired: Boolean) : Events()
        object ShowProgress : Events()
        object DismissProgress : Events()
        object OnErrorUserNotExist : Events()
        object RestartWithLocalDebtFragment : Events()
        class OnError(@StringRes val id: Int) : Events()
        class SetOptionsMenuEnabled(val isEnabled: Boolean) : Events()
    }
}