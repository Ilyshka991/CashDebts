package com.pechuro.cashdebts.data.data.repositories

import com.google.firebase.firestore.DocumentChange
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IRemoteDebtRepository {
    fun getSource(): Observable<DocumentChange>

    fun get(id: String): Single<FirestoreRemoteDebt>

    fun add(debt: FirestoreRemoteDebt, id: String? = null): Completable
}