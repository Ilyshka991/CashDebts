package com.pechuro.cashdebts.data.data.repositories

import com.google.firebase.firestore.DocumentChange
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import io.reactivex.Completable
import io.reactivex.Observable

interface IDebtRepository {
    fun getRemoteDebtSource(): Observable<DocumentChange>

    fun getLocalDebtSource(): Observable<List<FirestoreLocalDebt>>

    fun add(debt: FirestoreRemoteDebt): Completable

    fun add(debt: FirestoreLocalDebt): Completable
}