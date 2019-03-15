package com.pechuro.cashdebts.data.repositories

import com.google.firebase.FirebaseException
import io.reactivex.subjects.PublishSubject

interface IAuthRepository {
    val eventEmitter: PublishSubject<AuthEvents>

    fun isUserSignedIn(): Boolean

    fun startVerification(phoneNumber: String)

    fun resendCode(phoneNumber: String)

    fun verifyWithCode(code: String)

    fun signOut()
}

sealed class AuthEvents {
    class OnError(val e: FirebaseException) : AuthEvents()
    object OnCodeSent : AuthEvents()
    object OnSuccess : AuthEvents()
    object OnIncorrectCode : AuthEvents()
}