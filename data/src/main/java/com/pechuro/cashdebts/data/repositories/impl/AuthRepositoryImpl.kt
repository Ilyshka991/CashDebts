package com.pechuro.cashdebts.data.repositories.impl

import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.pechuro.cashdebts.data.exception.AuthInvalidCredentialsException
import com.pechuro.cashdebts.data.exception.AuthNotAvailableException
import com.pechuro.cashdebts.data.exception.AuthUnknownException
import com.pechuro.cashdebts.data.model.UserBaseInformation
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authClient: FirebaseAuth,
    private val phoneAuthProvider: PhoneAuthProvider
) : IAuthRepository {
    override val eventEmitter: PublishSubject<IAuthRepository.Event>
        get() = _eventEmitter

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var isCodeResent = false

    private val _eventEmitter = PublishSubject.create<IAuthRepository.Event>()

    private val authCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signIn(credential)
        }


        override fun onVerificationFailed(e: FirebaseException) {
            _eventEmitter.onNext(
                IAuthRepository.Event.OnError(
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> AuthInvalidCredentialsException()
                        is FirebaseTooManyRequestsException -> AuthNotAvailableException()
                        else -> AuthUnknownException()
                    }
                )
            )
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            if (!isCodeResent) {
                _eventEmitter.onNext(IAuthRepository.Event.OnCodeSent)
            }
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    override fun isUserSignedIn() = authClient.currentUser != null

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

    override fun getCurrentUserBaseInformation() = authClient.currentUser?.getBaseInformation()

    private fun signIn(credential: PhoneAuthCredential) {
        authClient.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _eventEmitter.onNext(if (task.isSuccessful) IAuthRepository.Event.OnSuccess else IAuthRepository.Event.OnIncorrectCode)
            }
    }

    private fun FirebaseUser.getBaseInformation() = UserBaseInformation(uid, phoneNumber!!)

    companion object {
        private const val TIMEOUT = 60L
    }
}
