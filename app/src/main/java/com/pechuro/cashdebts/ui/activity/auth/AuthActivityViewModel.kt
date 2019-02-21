package com.pechuro.cashdebts.ui.activity.auth

import androidx.databinding.ObservableBoolean
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.SingleLiveEvent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor() : BaseViewModel() {
    val command = SingleLiveEvent<Events>()

    val isLoading = ObservableBoolean()

    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val authCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            println("COMPLETE")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            println("FAILED")
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                }
                is FirebaseTooManyRequestsException -> {
                }
            }
        }

        override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {
            isLoading.set(false)
            command.call(Events.OnStartVerification)
            println("CODE SENT")
            storedVerificationId = verificationId
            resendToken = token
        }

        override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
            println("CODE TIMEOUT")
            storedVerificationId = verificationId
        }
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        isLoading.set(true)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback
        )
    }

    fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback,
            token
        )
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    command.call(Events.OnSuccess)
                    println("SING SUCCESS")
                } else {
                    println("SIGN FAILED")
                }
            }
    }

    companion object {
        private const val TIMEOUT = 60L
    }
}

sealed class Events {
    object OnStartVerification : Events()
    object OnSuccess : Events()
}
