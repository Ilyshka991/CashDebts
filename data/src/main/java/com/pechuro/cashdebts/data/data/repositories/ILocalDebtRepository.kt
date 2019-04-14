package com.pechuro.cashdebts.data.data.repositories

import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ILocalDebtRepository {

    fun getSource(): Observable<Map<String, FirestoreLocalDebt>>

    fun get(id: String): Single<FirestoreLocalDebt>

    fun add(debt: FirestoreLocalDebt, id: String? = null): Completable

    fun delete(id: String): Completable
}