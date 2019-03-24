package com.pechuro.cashdebts.data.repositories.impl

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.repositories.IDebtRepository
import com.pechuro.cashdebts.data.structure.FirestoreStructure
import com.pechuro.cashdebts.data.structure.FirestoreStructure.RemoteDebt.Structure.creditor
import com.pechuro.cashdebts.data.structure.FirestoreStructure.RemoteDebt.Structure.debtor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
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

    override fun add(debt: FirestoreRemoteDebt) = Completable.create { emitter ->
        debt.apply {
            creditorName = "ilya"
            creditorPhone = ""
            date = Date()
            status = FirestoreDebtStatus.WAIT_FOR_CONFIRMATION
        }
        FirebaseFirestore.getInstance().collection(FirestoreStructure.RemoteDebt.TAG)
            .add(debt).addOnSuccessListener {
                emitter.onComplete()
            }
    }
}