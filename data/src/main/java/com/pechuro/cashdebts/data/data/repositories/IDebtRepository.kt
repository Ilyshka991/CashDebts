package com.pechuro.cashdebts.data.data.repositories

import com.google.firebase.firestore.DocumentChange
import io.reactivex.Completable
import io.reactivex.Observable

interface IDebtRepository {
    fun getDataSource(): Observable<DocumentChange>

    fun add(debt: com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt): Completable

    fun add(debt: com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt): Completable
}