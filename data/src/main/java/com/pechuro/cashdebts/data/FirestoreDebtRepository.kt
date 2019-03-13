package com.pechuro.cashdebts.data

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.model.FirestoreDebt
import com.pechuro.cashdebts.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.structure.FirestoreStructure
import com.pechuro.cashdebts.data.structure.FirestoreStructure.Debts.Structure.creditor
import com.pechuro.cashdebts.data.structure.FirestoreStructure.Debts.Structure.debtor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class FirestoreDebtRepository @Inject constructor(private val store: FirebaseFirestore, private val user: CurrentUser) {

    fun getDataSource(): Observable<DocumentChange> = Observable.create<DocumentChange> { emitter ->
        store.collection(FirestoreStructure.Debts.TAG)
            .whereEqualTo(creditor, user.phoneNumber)
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                querySnapshot?.documentChanges?.forEach {
                    emitter.onNext(it)
                }
            }
        store.collection(FirestoreStructure.Debts.TAG)
            .whereEqualTo(debtor, user.phoneNumber)
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                querySnapshot?.documentChanges?.forEach {
                    emitter.onNext(it)
                }
            }
    }
        .subscribeOn(Schedulers.io())

    fun add(debt: FirestoreDebt) = Completable.create { emitter ->
        debt.apply {
            creditorName = "ilya"
            creditorPhone = user.phoneNumber!!
            date = Date()
            status = FirestoreDebtStatus.WAIT_FOR_CONFIRMATION
        }
        FirebaseFirestore.getInstance().collection(FirestoreStructure.Debts.TAG)
            .add(debt).addOnSuccessListener {
                emitter.onComplete()
            }
    }
}