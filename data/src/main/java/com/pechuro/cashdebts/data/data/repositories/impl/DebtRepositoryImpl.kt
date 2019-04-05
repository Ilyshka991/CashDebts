package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.creditor
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.debtor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class DebtRepositoryImpl @Inject constructor(private val store: FirebaseFirestore) : IDebtRepository {

    override fun getDataSource(): Observable<DocumentChange> = Observable.create<DocumentChange> { emitter ->
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

    override fun add(debt: com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt) = Completable.create { emitter ->
        FirebaseFirestore.getInstance().collection(FirestoreStructure.RemoteDebt.TAG)
            .add(debt).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreCommonException())
                }
            }
    }

    override fun add(debt: com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt) = Completable.create { emitter ->
        FirebaseFirestore.getInstance().collection(FirestoreStructure.LocalDebt.TAG)
            .add(debt).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreCommonException())
                }
            }
    }
}