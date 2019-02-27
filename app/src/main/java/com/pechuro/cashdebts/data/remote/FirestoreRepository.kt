package com.pechuro.cashdebts.data.remote

import android.annotation.SuppressLint
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.local.database.dao.DebtDao
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.data.model.User
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class FirestoreRepository(private val dao: DebtDao, private val firestore: FirebaseFirestore) {

    @SuppressLint("CheckResult")
    fun startSync() {
        Observable.create<DocumentChange> { emitter ->
            firestore.collection(FireStoreStructure.Debts.TAG).whereEqualTo("debtor", User.currentUserPhone)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    querySnapshot?.documentChanges?.forEach {
                        emitter.onNext(it)
                    }
                }
            firestore.collection(FireStoreStructure.Debts.TAG).whereEqualTo("debtee", User.currentUserPhone)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    querySnapshot?.documentChanges?.forEach {
                        emitter.onNext(it)
                    }
                }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                println("AAAAAAAAAAAAAAAASAFASFASFFAS")
                when (it.type) {
                    DocumentChange.Type.ADDED -> {
                        dao.add(it.document.getDebt())
                    }
                    DocumentChange.Type.REMOVED -> {
                    }
                    DocumentChange.Type.MODIFIED -> {
                    }
                }
            }, {
                println("ERRRRRRRRRRRRRRRRRRRRRRRRRRROR")
                println(it.message)
            })
    }

    private fun DocumentSnapshot.getDebt() = Debt(
        id,
        getString("debtor")!!,
        getString("debtee")!!,
        getDouble("value")!!,
        getString("description")
    )
}