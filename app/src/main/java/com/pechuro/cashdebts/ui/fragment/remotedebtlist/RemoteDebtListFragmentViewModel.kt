package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETE
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.CONFIRMATION_REJECTED
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.EDIT_CONFIRMATION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.EDIT_CONFIRMATION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.IN_PROGRESS
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETE_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETE_FROM_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_CONFIRMATION
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.repositories.IRemoteDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtListAdapter
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebtDiffCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RemoteDebtListFragmentViewModel @Inject constructor(
    private val debtRepository: IRemoteDebtRepository,
    private val userRepository: IUserRepository,
    private val diffCallback: RemoteDebtDiffCallback
) : BaseViewModel() {

    val command = PublishSubject.create<Command>()

    val debtSource = debtRepository.getSource()
        .subscribeOn(Schedulers.io())
        .concatMapSingle { map ->
            Observable.fromIterable(map.toList())
                .flatMapSingle { originData ->
                    val (id, firestoreDebt) = originData

                    val anotherPersonUid: String
                    val isCurrentUserCreditor: Boolean
                    if (firestoreDebt.creditorUid == userRepository.currentUserBaseInformation.uid) {
                        anotherPersonUid = firestoreDebt.debtorUid
                        isCurrentUserCreditor = true
                    } else {
                        anotherPersonUid = firestoreDebt.creditorUid
                        isCurrentUserCreditor = false
                    }

                    userRepository.getSingle(anotherPersonUid)
                        .map {
                            RemoteDebt.User(
                                anotherPersonUid,
                                it.firstName,
                                it.lastName,
                                it.phoneNumber,
                                it.photoUrl
                            )
                        }
                        .map {
                            RemoteDebt(
                                id,
                                it,
                                firestoreDebt.value,
                                firestoreDebt.description,
                                firestoreDebt.date,
                                firestoreDebt.status,
                                if (isCurrentUserCreditor) DebtRole.CREDITOR else DebtRole.DEBTOR,
                                firestoreDebt.initPersonUid == userRepository.currentUserBaseInformation.uid
                            )
                        }
                }.toList()
        }
        .observeOn(Schedulers.computation())
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
        .filter { diffCallback.oldList != it }
        .map { list ->
            list.sortedByDescending { it.date }
        }
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

    fun completeDebt(debt: RemoteDebt) {
        val status =
            if (debt.role == DebtRole.DEBTOR) WAIT_FOR_COMPLETE_FROM_CREDITOR else WAIT_FOR_COMPLETE_FROM_DEBTOR
        val firestoreDebt = debt.toFirestoreDebt(status)
        debtRepository.update(debt.id, firestoreDebt).subscribe({
            command.onNext(Command.ShowMessage(R.string.msg_info_sent))
        }, {
            command.onNext(Command.ShowMessage(R.string.error_load))
        }).addTo(compositeDisposable)
    }

    fun updateDebt(info: Pair<RemoteDebtListAdapter.Actions, RemoteDebt>) {
        val status = getDebtStatus(info.first, info.second.status)
        if (status == -1) {
            deleteDebt(info.second.id)
            return
        }
        val firestoreDebt = info.second.toFirestoreDebt(status)
        debtRepository.update(info.second.id, firestoreDebt).subscribe({
            command.onNext(Command.ShowMessage(R.string.msg_updated))
        }, {
            command.onNext(Command.ShowMessage(R.string.error_load))
        }).addTo(compositeDisposable)
    }

    private fun deleteDebt(id: String) {
        debtRepository.delete(id).subscribe({
            command.onNext(Command.ShowMessage(R.string.msg_deleted))
        }, {
            command.onNext(Command.ShowMessage(R.string.error_load))
        }).addTo(compositeDisposable)
    }

    private fun RemoteDebt.toFirestoreDebt(
        status: Int = this.status
    ) = FirestoreRemoteDebt(
        if (role == DebtRole.CREDITOR) userRepository.currentUserBaseInformation.uid else user.uid,
        if (role == DebtRole.DEBTOR) userRepository.currentUserBaseInformation.uid else user.uid,
        value,
        description,
        date,
        status,
        if (isCurrentUserInit) userRepository.currentUserBaseInformation.uid else user.uid
    )

    private fun getDebtStatus(action: RemoteDebtListAdapter.Actions, currentStatus: Int) =
        when (action) {
            RemoteDebtListAdapter.Actions.ACCEPT -> when (currentStatus) {
                WAIT_FOR_CONFIRMATION -> IN_PROGRESS
                WAIT_FOR_COMPLETE_FROM_CREDITOR, WAIT_FOR_COMPLETE_FROM_DEBTOR -> COMPLETE
                WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR, WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR -> IN_PROGRESS
                else -> throw IllegalArgumentException()
            }
            RemoteDebtListAdapter.Actions.REJECT -> when (currentStatus) {
                WAIT_FOR_CONFIRMATION -> CONFIRMATION_REJECTED
                WAIT_FOR_COMPLETE_FROM_CREDITOR -> COMPLETION_REJECTED_BY_CREDITOR
                WAIT_FOR_COMPLETE_FROM_DEBTOR -> COMPLETION_REJECTED_BY_DEBTOR
                WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR -> EDIT_CONFIRMATION_REJECTED_BY_CREDITOR
                WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR -> EDIT_CONFIRMATION_REJECTED_BY_DEBTOR
                else -> throw IllegalArgumentException()
            }
            RemoteDebtListAdapter.Actions.OK -> when (currentStatus) {
                CONFIRMATION_REJECTED -> -1
                else -> IN_PROGRESS
            }
            RemoteDebtListAdapter.Actions.DELETE -> -1
        }

    sealed class Command {
        class ShowMessage(@StringRes val msgId: Int) : Command()
    }
}