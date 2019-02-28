package com.pechuro.cashdebts.data

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.FirestoreStructure.Debts.Structure.creditor
import com.pechuro.cashdebts.data.FirestoreStructure.Debts.Structure.debtor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class FirestoreRepository(private val store: FirebaseFirestore, private val user: CurrentUser) {

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
}