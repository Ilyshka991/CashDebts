package com.pechuro.cashdebts.ui.activity.auth

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor() : BaseViewModel() {
    val command = PublishSubject.create<Events>()

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
            isLoading.set(false)
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    command.onNext(Events.ShowSnackBarError(R.string.error_auth_phone_validation))
                }
                is FirebaseTooManyRequestsException -> {
                    command.onNext(Events.ShowSnackBarError(R.string.error_auth_too_many_requests))
                }
                else -> {
                    command.onNext(Events.ShowSnackBarError(R.string.error_auth_common))
                }
            }
        }

        override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {
            isLoading.set(false)
            command.onNext(Events.OnStartVerification)
            println("CODE SENT")
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    fun startPhoneNumberVerification(phoneNumber: String?) {
        if (phoneNumber.isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_phone_validation))
            return
        }
        isLoading.set(true)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback
        )
    }

    fun verifyPhoneNumberWithCode(code: String?) {
        when {
            code.isNullOrEmpty() -> command.onNext(Events.ShowSnackBarError(R.string.error_auth_code_validation))
            storedVerificationId == null -> command.onNext(Events.ShowSnackBarError(R.string.error_auth_common))
            else -> verifyPhoneNumberWithCode(storedVerificationId!!, code)
        }
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

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        isLoading.set(true)
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    command.onNext(Events.OnSuccess)
                    println("SING SUCCESS")
                } else {
                    command.onNext(Events.ShowSnackBarError(R.string.error_auth_code_validation))
                    isLoading.set(false)
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
    class ShowSnackBarError(@StringRes val id: Int) : Events()
}
