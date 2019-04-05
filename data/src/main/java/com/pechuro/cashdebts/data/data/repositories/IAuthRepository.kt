package com.pechuro.cashdebts.data.data.repositories

import io.reactivex.subjects.PublishSubject

interface IAuthRepository {
    val eventEmitter: PublishSubject<Event>

    fun isUserSignedIn(): Boolean

    fun startVerification(phoneNumber: String)

    fun resendCode(phoneNumber: String)

    fun verifyWithCode(code: String)

    fun signOut()

    fun getCurrentUserBaseInformation(): com.pechuro.cashdebts.data.data.model.UserBaseInformation?

    sealed class Event {
        class OnError(val e: com.pechuro.cashdebts.data.data.exception.AuthException) : Event()
        object OnCodeSent : Event()
        object OnSuccess : Event()
        object OnIncorrectCode : Event()
    }
}

