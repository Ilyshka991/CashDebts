package com.pechuro.cashdebts.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.local.database.dao.DebtDao
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.debtee
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.debtor
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.description
import com.pechuro.cashdebts.data.remote.FirestoreStructure.Debts.Structure.value
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FirestoreRepository(
    private val dao: DebtDao,
    private val store: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val compositeDisposable = CompositeDisposable()

    fun startSync() {
        Observable.create<DocumentChange> { emitter ->
            store.collection(FirestoreStructure.Debts.TAG)
                .whereEqualTo(debtor, getCurrentUserPhone())
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    querySnapshot?.documentChanges?.forEach {
                        emitter.onNext(it)
                    }
                }
            store.collection(FirestoreStructure.Debts.TAG)
                .whereEqualTo(debtee, getCurrentUserPhone())
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    querySnapshot?.documentChanges?.forEach {
                        emitter.onNext(it)
                    }
                }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
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
                println(it.message)
            })
            .let(compositeDisposable::add)
    }

    fun stopSync() {
        compositeDisposable.dispose()
    }

    fun getInitialData() {
        store.collection(FirestoreStructure.Debts.TAG)
            .whereEqualTo(debtor, getCurrentUserPhone())
            .get()
            .addOnSuccessListener {
                it.documents.forEach { document ->
                    dao.add(document.getDebt())
                }
            }
    }

    private fun getCurrentUserPhone() = auth.currentUser?.phoneNumber

    private fun DocumentSnapshot.getDebt(): Debt {
        val isCurrentUserDebtor = getString(debtor) == getCurrentUserPhone()
        return Debt(
            id,
            if (isCurrentUserDebtor) getString(debtee)!! else getString(debtor)!!,
            getDouble(value)!!,
            getString(description)
        )
    }
}