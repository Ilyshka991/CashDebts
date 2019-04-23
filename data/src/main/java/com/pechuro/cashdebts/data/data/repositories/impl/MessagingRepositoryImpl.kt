package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.messaging.FirebaseMessaging
import com.pechuro.cashdebts.data.data.repositories.IMessagingRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MessagingRepositoryImpl @Inject constructor(
    private val messagingClient: FirebaseMessaging
) : IMessagingRepository {

    override fun setEnabled(isEnabled: Boolean) {
        messagingClient.isAutoInitEnabled = isEnabled
    }

    override fun getToken(): Single<String> {
        return Single.just("")
    }

    override fun deleteToken(personUid: String): Completable {
        return Completable.complete()
    }

    override fun saveToken(personUid: String, token: String?): Completable {
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        return Completable.complete()
    }
}