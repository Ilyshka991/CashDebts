package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.creditor
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.debtor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class DebtRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val userRepository: IUserRepository
) : IDebtRepository {

    //TODO: remake this
    override fun getRemoteDebtSource(): Observable<DocumentChange> = Observable.create<DocumentChange> { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG)
            .whereEqualTo(creditor, "")
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                querySnapshot?.documentChanges?.forEach {
                    emitter.onNext(it)
                }
            }
        store.collection(FirestoreStructure.RemoteDebt.TAG)
            .whereEqualTo(debtor, "")
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                querySnapshot?.documentChanges?.forEach {
                    emitter.onNext(it)
                }
            }
    }
        .subscribeOn(Schedulers.io())

    override fun getLocalDebtSource() = Observable.create<Map<String, FirestoreLocalDebt>> { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG)
            .whereEqualTo(
                FirestoreStructure.LocalDebt.Structure.ownerUid,
                userRepository.currentUserBaseInformation.uid
            )
            .addSnapshotListener { snapshot, exception ->
                if (snapshot == null) return@addSnapshotListener
                snapshot.documents
                    .mapNotNull {
                        it.id to it.toObject(FirestoreLocalDebt::class.java)!!
                    }
                    .toMap()
                    .let {
                        if (!emitter.isDisposed) emitter.onNext(it)
                    }
            }
    }

    override fun add(debt: FirestoreRemoteDebt) = Completable.create { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG)
            .add(debt).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }

    override fun add(debt: FirestoreLocalDebt, id: String?) = Completable.create { emitter ->
        if (id != null) {
            store.collection(FirestoreStructure.LocalDebt.TAG)
                .document(id)
                .set(debt).addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }
        } else {
            store.collection(FirestoreStructure.LocalDebt.TAG)
                .add(debt).addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }
        }
    }

    override fun deleteLocalDebt(id: String) = Completable.create { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG).document(id).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                if (!emitter.isDisposed) emitter.onComplete()
            } else {
                if (!emitter.isDisposed) emitter.onError(FirestoreCommonException())
            }
        }
    }

    override fun getLocalDebt(id: String) = Single.create<FirestoreLocalDebt> { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG).document(id).get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                if (!emitter.isDisposed) {
                    emitter.onSuccess(it.result!!.toObject(FirestoreLocalDebt::class.java)!!)
                }
            } else {
                if (!emitter.isDisposed) {
                    emitter.onError(FirestoreCommonException())
                }
            }
        }
    }

    override fun getRemoteDebt(id: String) = Single.create<FirestoreRemoteDebt> { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG).document(id).get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                if (!emitter.isDisposed) {
                    emitter.onSuccess(it.result!!.toObject(FirestoreRemoteDebt::class.java)!!)
                }
            } else {
                if (!emitter.isDisposed) {
                    emitter.onError(FirestoreCommonException())
                }
            }
        }
    }
}