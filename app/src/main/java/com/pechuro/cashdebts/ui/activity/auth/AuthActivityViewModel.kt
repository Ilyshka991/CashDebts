package com.pechuro.cashdebts.ui.activity.auth

import android.telephony.TelephonyManager
import androidx.annotation.StringRes
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.exception.AuthException
import com.pechuro.cashdebts.data.exception.AuthInvalidCredentialsException
import com.pechuro.cashdebts.data.exception.AuthNotAvailableException
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val telephonyManager: TelephonyManager,
    private val prefsManager: PrefsManager
) : BaseViewModel() {
    val command = PublishSubject.create<Events>()

    var phoneNumber = BehaviorSubject.create<String>()
    var phoneCode = BehaviorSubject.create<String>()
    var countryData = BehaviorSubject.create<CountryData>()

    init {
        subscribeToEvents()
    }

    fun startPhoneNumberVerification() {
        val number = phoneNumber.value
        if (number.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.error_auth_phone_validation))
            return
        }
        command.onNext(Events.OnStartLoading)
        authRepository.startVerification(number)
    }

    fun verifyPhoneNumberWithCode() {
        val code = phoneCode.value
        if (code.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.error_auth_code_validation))
            return
        }
        command.onNext(Events.OnStartLoading)
        authRepository.verifyWithCode(code)
    }

    fun resendVerificationCode() {
        val number = phoneNumber.value
        if (number.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.error_auth_phone_validation))
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

    private fun subscribeToEvents() {
        authRepository.eventEmitter.subscribe {
            when (it) {
                is IAuthRepository.Event.OnError -> onError(it.e)
                is IAuthRepository.Event.OnCodeSent -> onCodeSent()
                is IAuthRepository.Event.OnSuccess -> onSuccess()
                is IAuthRepository.Event.OnIncorrectCode -> onIncorrectCode()
            }
        }.addTo(compositeDisposable)
    }

    private fun onError(e: AuthException) {
        command.onNext(Events.OnStopLoading)
        val error = when (e) {
            is AuthInvalidCredentialsException -> R.string.error_auth_phone_validation
            is AuthNotAvailableException -> R.string.error_auth_too_many_requests
            else -> R.string.error_auth_common
        }
        command.onNext(Events.OnError(error))
    }

    private fun onCodeSent() {
        command.onNext(Events.OnStopLoading)
        command.onNext(Events.OnCodeSent)
    }

    private fun onSuccess() {
        userRepository.isUserWithUidExist()
            .subscribe({
                command.onNext(Events.OnStopLoading)
                if (it) {
                    prefsManager.isUserAddInfo = true
                    command.onNext(Events.OnComplete)
                } else {
                    command.onNext(Events.OpenProfileEdit)
                }
            }, {
                command.onNext(Events.OnStopLoading)
            })
            .addTo(compositeDisposable)
    }

    private fun onIncorrectCode() {
        command.onNext(Events.OnError(R.string.error_auth_code_validation))
        command.onNext(Events.OnStopLoading)
    }

    sealed class Events {
        object OnCodeSent : Events()
        object OnComplete : Events()
        object OpenProfileEdit : Events()
        class OnError(@StringRes val id: Int) : Events()
        object OnStartLoading : Events()
        object OnStopLoading : Events()
    }
}