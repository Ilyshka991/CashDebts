package com.pechuro.cashdebts.data.remote

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.model.CurrentUser
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.debtee
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.debtor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class FirestoreRepository(private val store: FirebaseFirestore, private val user: CurrentUser) {

    fun getDataSource(): Observable<DocumentChange> = Observable.create<DocumentChange> { emitter ->
        store.collection(FirestoreStructure.Debts.TAG)
            .whereEqualTo(debtor, user.phoneNumber)
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                querySnapshot?.documentChanges?.forEach {
                    emitter.onNext(it)
                }
            }
        store.collection(FirestoreStructure.Debts.TAG)
            .whereEqualTo(debtee, user.phoneNumber)
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                querySnapshot?.documentChanges?.forEach {
                    emitter.onNext(it)
                }
            }
    }
        .subscribeOn(Schedulers.io())
}