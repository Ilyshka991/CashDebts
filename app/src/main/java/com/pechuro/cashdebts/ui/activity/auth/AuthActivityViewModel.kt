package com.pechuro.cashdebts.ui.activity.auth

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor() : BaseViewModel() {
    val command = PublishSubject.create<Events>()

    val isLoading = ObservableBoolean()
    val phoneNumber = ObservableField<String?>()
    val phoneCode = ObservableField<String?>()
    val countryData = ObservableField<CountryData?>()

    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var isCodeResended = false

    private val authCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
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
            if (!isCodeResended) command.onNext(Events.OnCodeSent)
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    fun startPhoneNumberVerification() {
        if (phoneNumber.get().isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_phone_validation))
            return
        }
        isLoading.set(true)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber.get()!!,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback
        )
    }

    fun verifyPhoneNumberWithCode() {
        when {
            phoneCode.get().isNullOrEmpty() -> command.onNext(Events.ShowSnackBarError(R.string.error_auth_code_validation))
            storedVerificationId == null -> command.onNext(Events.ShowSnackBarError(R.string.error_auth_common))
            else -> verifyPhoneNumberWithCode(storedVerificationId!!, phoneCode.get()!!)
        }
    }

    fun resendVerificationCode() {
        if (phoneNumber.get().isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_phone_validation))
            return
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber.get()!!,
            TIMEOUT,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            authCallback,
            resendToken
        )
        isCodeResended = true
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
                } else {
                    command.onNext(Events.ShowSnackBarError(R.string.error_auth_code_validation))
                    isLoading.set(false)
                }
            }
    }

    companion object {
        private const val TIMEOUT = 60L
    }
}

sealed class Events {
    object OnCodeSent : Events()
    object OnSuccess : Events()
    object OpenCountrySelection : Events()
    object CloseCountrySelection : Events()
    class ShowSnackBarError(@StringRes val id: Int) : Events()
}
