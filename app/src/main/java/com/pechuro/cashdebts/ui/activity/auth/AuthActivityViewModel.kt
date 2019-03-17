package com.pechuro.cashdebts.ui.activity.auth

import android.content.SharedPreferences
import android.telephony.TelephonyManager
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.exception.AuthException
import com.pechuro.cashdebts.data.exception.AuthInvalidCredentialsException
import com.pechuro.cashdebts.data.exception.AuthNotAvailableException
import com.pechuro.cashdebts.data.repositories.AuthEvents
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.model.prefs.PrefsKey
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val telephonyManager: TelephonyManager,
    private val prefs: SharedPreferences
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
        authRepository.eventEmitter.subscribe {
            when (it) {
                is AuthEvents.OnError -> onError(it.e)
                is AuthEvents.OnCodeSent -> onCodeSent()
                is AuthEvents.OnSuccess -> onSuccess()
                is AuthEvents.OnIncorrectCode -> onIncorrectCode()
            }
        }.addTo(compositeDisposable)
    }

    private fun onError(e: AuthException) {
        isLoading.set(false)
        val error = when (e) {
            is AuthInvalidCredentialsException -> R.string.error_auth_phone_validation
            is AuthNotAvailableException -> R.string.error_auth_too_many_requests
            else -> R.string.error_auth_common
        }
        command.onNext(Events.ShowSnackBarError(error))
    }

    private fun onCodeSent() {
        isLoading.set(false)
        command.onNext(Events.OnCodeSent)
    }

    private fun onSuccess() {
        userRepository.isUserExist()
            .subscribe({
                isLoading.set(false)
                if (it) {
                    prefs.edit {
                        putBoolean(PrefsKey.IS_USER_ADD_INFO, true)
                    }
                    command.onNext(Events.OnComplete)
                } else {
                    command.onNext(Events.OpenProfileEdit)
                }
            }, {
                isLoading.set(false)
            })
            .addTo(compositeDisposable)
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
        authRepository.startVerification(number)
    }

    fun verifyPhoneNumberWithCode() {
        val code = phoneCode.get()
        if (code.isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_code_validation))
            return
        }
        isLoading.set(true)
        authRepository.verifyWithCode(code)
    }

    fun resendVerificationCode() {
        val number = phoneNumber.get()
        if (number.isNullOrEmpty()) {
            command.onNext(Events.ShowSnackBarError(R.string.error_auth_phone_validation))
            return
        }
        authRepository.resendCode(number)
    }

    fun getUserCountryCode(): String? {
        val simCountry = telephonyManager.simCountryIso
        if (simCountry != null && simCountry.length == 2) {
            return simCountry.toUpperCase()
        } else if (telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
            val networkCountry = telephonyManager.networkCountryIso
            if (networkCountry != null && networkCountry.length == 2) {
                return networkCountry.toUpperCase()
            }
        }
        return null
    }

    sealed class Events {
        object OnCodeSent : Events()
        object OnComplete : Events()
        object OpenProfileEdit : Events()
        class ShowSnackBarError(@StringRes val id: Int) : Events()
    }
}