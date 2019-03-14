package com.pechuro.cashdebts.ui.activity.auth

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.repositories.AuthEvents
import com.pechuro.cashdebts.data.repositories.FirebaseAuthRepository
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
) : BaseViewModel() {
    val command = PublishSubject.create<Events>()

    val isLoading = ObservableBoolean()
    val phoneNumber = ObservableField<String?>()
    val phoneCode = ObservableField<String?>()
    val countryData = ObservableField<CountryData?>()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        repository.eventEmitter.subscribe {
            when (it) {
                is AuthEvents.OnError -> onError(it.e)
                is AuthEvents.OnCodeSent -> onCodeSent()
                is AuthEvents.OnSuccess -> onSuccess()
                is AuthEvents.OnIncorrectCode -> onIncorrectCode()
            }
        }.addTo(compositeDisposable)
    }

    private fun onError(e: FirebaseException) {
        val error = when (e) {
            is FirebaseAuthInvalidCredentialsException -> R.string.error_auth_phone_validation
            is FirebaseTooManyRequestsException -> R.string.error_auth_too_many_requests
            else -> R.string.error_auth_common
        }
        command.onNext(Events.ShowSnackBarError(error))
    }

    private fun onCodeSent() {
        isLoading.set(false)
        command.onNext(Events.OnCodeSent)
    }

    private fun onSuccess() {
        command.onNext(Events.OnSuccess)
    }

    private fun onIncorrectCode() {
        command.onNext(Events.ShowSnackBarError(R.string.error_auth_code_validation))
        isLoading.set(false)
    }

    fun startPhoneNumberVerification() {
        val number = phoneNumber.get()
        if (number.isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_phone_validation))
            return
        }
        isLoading.set(true)
        repository.startPhoneNumberVerification(number)
    }

    fun verifyPhoneNumberWithCode() {
        val code = phoneCode.get()
        if (code.isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_code_validation))
            return
        }
        repository.verifyPhoneNumberWithCode(code)
    }

    fun resendVerificationCode() {
        val number = phoneNumber.get()
        if (number.isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_phone_validation))
            return
        }
        repository.resendVerificationCode(number)
    }
}

sealed class Events {
    object OnCodeSent : Events()
    object OnSuccess : Events()
    class ShowSnackBarError(@StringRes val id: Int) : Events()
}
