package com.pechuro.cashdebts.data.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

interface IMessagingRepository {

    fun setEnabled(isEnabled: Boolean)

    fun getCurrentToken(): Single<String>

    fun deleteCurrentToken(personUid: String): Completable

    fun saveToken(personUid: String, token: String? = null): Completable
}