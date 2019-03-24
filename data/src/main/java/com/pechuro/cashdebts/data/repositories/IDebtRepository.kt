package com.pechuro.cashdebts.data.repositories

import com.google.firebase.firestore.DocumentChange
import com.pechuro.cashdebts.data.model.FirestoreRemoteDebt
import io.reactivex.Completable
import io.reactivex.Observable

interface IDebtRepository {
    fun getDataSource(): Observable<DocumentChange>

    fun add(debt: FirestoreRemoteDebt): Completable
}