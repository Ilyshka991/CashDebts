package com.pechuro.cashdebts.data.repositories

import com.pechuro.cashdebts.data.model.FirestoreUser
import io.reactivex.Completable
import io.reactivex.Single

interface IUserRepository {
    fun get(uid: String): Single<FirestoreUser>

    fun isUserExist(uid: String): Single<Boolean>

    fun setUser(uid: String, user: FirestoreUser): Completable
}