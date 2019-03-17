package com.pechuro.cashdebts.data.repositories

import com.pechuro.cashdebts.data.exception.AuthException
import com.pechuro.cashdebts.data.model.UserBaseInformation
import io.reactivex.subjects.PublishSubject

interface IAuthRepository {
    val eventEmitter: PublishSubject<AuthEvents>

    fun isUserSignedIn(): Boolean

    fun startVerification(phoneNumber: String)

    fun resendCode(phoneNumber: String)

    fun verifyWithCode(code: String)

    fun signOut()

    fun getCurrentUserBaseInformation(): UserBaseInformation?
}

sealed class AuthEvents {
    class OnError(val e: AuthException) : AuthEvents()
    object OnCodeSent : AuthEvents()
    object OnSuccess : AuthEvents()
    object OnIncorrectCode : AuthEvents()
}