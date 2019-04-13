package com.pechuro.cashdebts.data.data.repositories

import com.google.firebase.firestore.DocumentChange
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IDebtRepository {
    fun getRemoteDebtSource(): Observable<DocumentChange>

    fun getLocalDebtSource(): Observable<Map<String, FirestoreLocalDebt>>

    fun getRemoteDebt(id: String): Single<FirestoreRemoteDebt>

    fun getLocalDebt(id: String): Single<FirestoreLocalDebt>

    fun add(debt: FirestoreRemoteDebt): Completable

    fun add(debt: FirestoreLocalDebt, id: String? = null): Completable

    fun deleteLocalDebt(id: String): Completable
}