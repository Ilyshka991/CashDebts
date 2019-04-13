package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.calculator.Calculator
import com.pechuro.cashdebts.calculator.Result
import com.pechuro.cashdebts.data.data.exception.FirestoreUserNotFoundException
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.CREDITOR
import com.pechuro.cashdebts.data.data.model.DebtRole.Companion.DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreBaseDebt
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.connectivity.ConnectivityListener
import com.pechuro.cashdebts.ui.activity.adddebt.model.BaseDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.LocalDebtInfo
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.RemoteDebtInfo
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.requireValue
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddDebtActivityViewModel @Inject constructor(
    private val debtRepository: IDebtRepository,
    private val userRepository: IUserRepository,
    private val connectivityListener: ConnectivityListener,
    private val calculator: Calculator
) : BaseViewModel() {
    val command = PublishSubject.create<Events>()

    val isConnectionAvailable = BehaviorSubject.create<Boolean>()
    val mathExpression = BehaviorSubject.createDefault("0.0")

    val debtValue: Observable<Pair<Boolean, Result>> by lazy {
        mathExpression
            .map { isNotMathExpression(it) to calculator.evaluate(it) }
            .subscribeOn(Schedulers.computation())
            .also { observable ->
                observable
                    .map {
                        when (val result = it.second) {
                            is Result.Success -> result.result
                            is Result.Error -> 0.0
                        }
                    }
                    .subscribe(debt.value)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    lateinit var debt: BaseDebtInfo

    init {
        setConnectivityListener()
    }

    fun setInitialData(isLocalDebt: Boolean) {
        if (!::debt.isInitialized) {
            debt = if (isLocalDebt) LocalDebtInfo() else RemoteDebtInfo()
        }
    }

    fun loadExistingDebt(id: String) {
        command.onNext(Events.ShowProgress)
        val debtInfo = debt
        val source = when (debtInfo) {
            is LocalDebtInfo -> debtRepository.getLocalDebt(id)
            is RemoteDebtInfo -> debtRepository.getRemoteDebt(id)
            else -> throw  IllegalArgumentException()
        }
        source.subscribe({
            //command.onNext(Events.OnDebtLoaded(it))
            (debt as LocalDebtInfo).name.onNext("dsf")
            command.onNext(Events.DismissProgress)
        }, {
            command.onNext(Events.DismissProgress)
        }).addTo(compositeDisposable)
        Observable.just("sg").delay(4, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                (debt as LocalDebtInfo).name.onNext(it)
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

        val sendingDebt = FirestoreRemoteDebt(
            creditorUid,
            debtorUid,
            debt.value.requireValue,
            debt.description.requireValue,
            debt.date.requireValue,
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
            debt.name.requireValue,
            debt.value.requireValue,
            debt.description.requireValue,
            debt.date.requireValue,
            debt.debtRole.requireValue,
            false
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
                is FirestoreUserNotFoundException -> command.onNext(Events.OnErrorUserNotExist)
                else -> command.onNext(Events.OnError(R.string.add_debt_error_common))
            }
        }).addTo(compositeDisposable)
    }

    private fun isNotMathExpression(expr: String) = expr.matches(NUMBER_REGEX)

    sealed class Events {
        object OnSaved : Events()
        class OpenInfo(val isInternetRequired: Boolean) : Events()
        object ShowProgress : Events()
        object DismissProgress : Events()
        object OnErrorUserNotExist : Events()
        object RestartWithLocalDebtFragment : Events()
        class OnError(@StringRes val id: Int) : Events()
        class SetOptionsMenuEnabled(val isEnabled: Boolean) : Events()
        class OnDebtLoaded(val debt: FirestoreBaseDebt) : Events()
    }

    companion object {
        private val NUMBER_REGEX = "-?\\d+(\\.\\d+)?".toRegex()
    }
}