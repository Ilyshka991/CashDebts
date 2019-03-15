package com.pechuro.cashdebts.data.repositories.impl

import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pechuro.cashdebts.data.repositories.AuthEvents
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authClient: FirebaseAuth,
    private val phoneAuthProvider: PhoneAuthProvider
) : IAuthRepository {
    override val eventEmitter: PublishSubject<AuthEvents>
        get() = PublishSubject.create<AuthEvents>()

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var isCodeResent = false

    private val authCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signIn(credential)
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

    override fun startVerification(phoneNumber: String) {
        phoneAuthProvider.verifyPhoneNumber(
            phoneNumber,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback
        )
    }

    override fun resendCode(phoneNumber: String) {
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

    override fun verifyWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signIn(credential)
    }

    override fun signOut() {
        authClient.signOut()
    }

    private fun signIn(credential: PhoneAuthCredential) {
        authClient.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                eventEmitter.onNext(if (task.isSuccessful) AuthEvents.OnSuccess else AuthEvents.OnIncorrectCode)
            }
    }

    companion object {
        private const val TIMEOUT = 60L
    }
}
