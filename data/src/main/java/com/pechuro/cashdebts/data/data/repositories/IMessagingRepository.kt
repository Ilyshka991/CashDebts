package com.pechuro.cashdebts.data.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

interface IMessagingRepository {

    fun setEnabled(isEnabled: Boolean)

    fun getToken(): Single<String>

    fun deleteToken(personUid: String): Completable

    fun saveToken(personUid: String, token: String? = null): Completable
}