package com.pechuro.cashdebts.data.repositories

import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val authClient: FirebaseAuth,
    private val phoneAuthProvider: PhoneAuthProvider
) {
    val eventEmitter = PublishSubject.create<AuthEvents>()

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var isCodeResent = false

    private val authCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }


        override fun onVerificationFailed(e: FirebaseException) {
            eventEmitter.onNext(AuthEvents.OnError(e))
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            if (!isCodeResent) eventEmitter.onNext(AuthEvents.OnCodeSent)
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        phoneAuthProvider.verifyPhoneNumber(
            phoneNumber,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback
        )
    }

    fun resendVerificationCode(phoneNumber: String) {
        phoneAuthProvider.verifyPhoneNumber(
            phoneNumber,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback,
            resendToken
        )
        isCodeResent = true
    }

    fun verifyPhoneNumberWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun signOut() {
        authClient.signOut()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        authClient.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                eventEmitter.onNext(if (task.isSuccessful) AuthEvents.OnSuccess else AuthEvents.OnIncorrectCode)
            }
    }

    companion object {
        private const val TIMEOUT = 60L
    }
}

sealed class AuthEvents {
    class OnError(val e: FirebaseException) : AuthEvents()
    object OnCodeSent : AuthEvents()
    object OnSuccess : AuthEvents()
    object OnIncorrectCode : AuthEvents()
}