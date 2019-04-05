package com.pechuro.cashdebts.data.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

interface IUserRepository {
    val currentUserBaseInformation: com.pechuro.cashdebts.data.data.model.UserBaseInformation

    fun get(uid: String = currentUserBaseInformation.uid): Single<com.pechuro.cashdebts.data.data.model.FirestoreUser>

    fun isUserWithUidExist(uid: String = currentUserBaseInformation.uid): Single<Boolean>

    fun getUidByPhone(phoneNumber: String): Single<String>

    fun updateUser(
        user: com.pechuro.cashdebts.data.data.model.FirestoreUser,
        uid: String = currentUserBaseInformation.uid
    ): Completable
}