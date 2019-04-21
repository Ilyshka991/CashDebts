package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.model.DebtDeleteStatus
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
import javax.inject.Inject

internal class RemoteDebtRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val userRepository: IUserRepository
) : IRemoteDebtRepository {

    override fun getSource() =
        Observable.combineLatest(
            getRemoteDebtSource(userRepository.currentUserBaseInformation.uid, creditorUid)
                .map { map ->
                    map.filter { it.value.deleteStatus != DebtDeleteStatus.DELETE_FROM_CREDITOR }
                },
            getRemoteDebtSource(userRepository.currentUserBaseInformation.uid, debtorUid)
                .map { map ->
                    map.filter { it.value.deleteStatus != DebtDeleteStatus.DELETE_FROM_DEBTOR }
                },
            BiFunction { creditor: Map<String, FirestoreRemoteDebt>, debtor: Map<String, FirestoreRemoteDebt> ->
                creditor to debtor
            }).map {
            it.first + it.second
        }

    override fun getSingle(id: String) = Single.create<FirestoreRemoteDebt> { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG).document(id).get()
            .addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful && it.result != null) {
                    emitter.onSuccess(it.result!!.toObject(FirestoreRemoteDebt::class.java)!!)
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }

    override fun add(debt: FirestoreRemoteDebt) = Completable.create { emitter ->
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

    override fun update(id: String, debt: FirestoreRemoteDebt) = Completable.create { emitter ->
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

    override fun delete(id: String) = Completable.create { emitter ->
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
                            it.id to it.toObject(FirestoreRemoteDebt::class.java)!!
                        }
                        .toMap()
                        .let {
                            if (!emitter.isDisposed) emitter.onNext(it)
                        }
                }
        }
}