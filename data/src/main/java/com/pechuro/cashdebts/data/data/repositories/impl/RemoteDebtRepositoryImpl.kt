package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.model.DebtDeleteStatus
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.repositories.IRemoteDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.creditorUid
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.debtorUid
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*
import javax.inject.Inject

internal class RemoteDebtRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val userRepository: IUserRepository
) : IRemoteDebtRepository {

    private val snapshotListeners = mutableListOf<ListenerRegistration>()

    override fun getSource(): Observable<Map<String, FirestoreRemoteDebt>> =
        Observable.combineLatest(
            getRemoteDebtSource(userRepository.currentUserBaseInformation.uid, creditorUid)
                .map { map ->
                    map.filter {
                        it.value.deleteStatus != DebtDeleteStatus.DELETED_FROM_CREDITOR &&
                                it.value.deleteStatus != DebtDeleteStatus.CACHED
                    }
                },
            getRemoteDebtSource(userRepository.currentUserBaseInformation.uid, debtorUid)
                .map { map ->
                    map.filter {
                        it.value.deleteStatus != DebtDeleteStatus.DELETED_FROM_DEBTOR &&
                                it.value.deleteStatus != DebtDeleteStatus.CACHED
                    }
                },
            BiFunction { creditor: Map<String, FirestoreRemoteDebt>, debtor: Map<String, FirestoreRemoteDebt> ->
                creditor to debtor
            }).map {
            it.first + it.second
        }.doOnDispose {
            snapshotListeners.forEach { it.remove() }
        }

    override fun getSingle(id: String): Single<FirestoreRemoteDebt> =
        Single.create<FirestoreRemoteDebt> { emitter ->
            store.collection(FirestoreStructure.RemoteDebt.TAG).document(id).get()
                .addOnCompleteListener {
                    if (emitter.isDisposed) return@addOnCompleteListener
                    if (it.isSuccessful && it.result != null) {
                        val debt = with(it.result!!) {
                            FirestoreRemoteDebt(
                                getString(FirestoreStructure.RemoteDebt.Structure.creditorUid)
                                    ?: "",
                                getString(FirestoreStructure.RemoteDebt.Structure.debtorUid) ?: "",
                                getDouble(FirestoreStructure.RemoteDebt.Structure.value) ?: 0.0,
                                getString(FirestoreStructure.RemoteDebt.Structure.description)
                                    ?: "",
                                getDate(FirestoreStructure.RemoteDebt.Structure.date) ?: Date(),
                                getLong(FirestoreStructure.RemoteDebt.Structure.status)?.toInt()
                                    ?: FirestoreDebtStatus.NOT_SEND,
                                getString(FirestoreStructure.RemoteDebt.Structure.initPersonUid)
                                    ?: "",
                                getLong(FirestoreStructure.RemoteDebt.Structure.deleteStatus)?.toInt()
                                    ?: DebtDeleteStatus.NOT_DELETED

                            )
                        }
                        emitter.onSuccess(debt)
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }
        }

    override fun add(debt: FirestoreRemoteDebt): Completable = Completable.create { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG)
            .add(debt).addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }

    override fun update(id: String, debt: FirestoreRemoteDebt): Completable =
        Completable.create { emitter ->
            store.collection(FirestoreStructure.RemoteDebt.TAG)
                .document(id)
                .set(debt)
                .addOnCompleteListener {
                    if (emitter.isDisposed) return@addOnCompleteListener
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }
        }

    override fun delete(id: String): Completable = Completable.create { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG)
            .document(id)
            .delete()
            .addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }

    private fun getRemoteDebtSource(uid: String, debtRole: String) =
        Observable.create<Map<String, FirestoreRemoteDebt>> { emitter ->
            store.collection(FirestoreStructure.RemoteDebt.TAG)
                .whereEqualTo(debtRole, uid)
                .addSnapshotListener { snapshot, e ->
                    if (snapshot == null) return@addSnapshotListener
                    snapshot.documents
                        .mapNotNull {
                            val debt = with(it) {
                                FirestoreRemoteDebt(
                                    getString(FirestoreStructure.RemoteDebt.Structure.creditorUid)
                                        ?: "",
                                    getString(FirestoreStructure.RemoteDebt.Structure.debtorUid)
                                        ?: "",
                                    getDouble(FirestoreStructure.RemoteDebt.Structure.value) ?: 0.0,
                                    getString(FirestoreStructure.RemoteDebt.Structure.description)
                                        ?: "",
                                    getDate(FirestoreStructure.RemoteDebt.Structure.date) ?: Date(),
                                    getLong(FirestoreStructure.RemoteDebt.Structure.status)?.toInt()
                                        ?: FirestoreDebtStatus.NOT_SEND,
                                    getString(FirestoreStructure.RemoteDebt.Structure.initPersonUid)
                                        ?: "",
                                    getLong(FirestoreStructure.RemoteDebt.Structure.deleteStatus)?.toInt()
                                        ?: DebtDeleteStatus.NOT_DELETED

                                )
                            }
                            it.id to debt
                        }
                        .toMap()
                        .let {
                            if (!emitter.isDisposed) emitter.onNext(it)
                        }
                }.also {
                    snapshotListeners += it
                }
        }
}