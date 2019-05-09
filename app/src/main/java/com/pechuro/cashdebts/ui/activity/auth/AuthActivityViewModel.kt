package com.pechuro.cashdebts.ui.activity.auth

import android.telephony.TelephonyManager
import androidx.annotation.StringRes
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.exception.AuthException
import com.pechuro.cashdebts.data.data.exception.AuthInvalidCredentialsException
import com.pechuro.cashdebts.data.data.exception.AuthNotAvailableException
import com.pechuro.cashdebts.data.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.extensions.getFormattedNumber
import com.pechuro.cashdebts.ui.utils.extensions.getUserCountryCode
import com.pechuro.cashdebts.ui.utils.extensions.requireValue
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor(
    val countryList: List<CountryData>,
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val prefsManager: PrefsManager,
    private val telephonyManager: TelephonyManager
) : BaseViewModel() {
    val command = PublishSubject.create<Events>()

    val fullPhoneNumber = BehaviorSubject.createDefault("")
    val phoneCode = BehaviorSubject.createDefault("")

    val loadingState = BehaviorSubject.createDefault(false)

    init {
        setAuthEventListener()
    }

    fun startPhoneNumberVerification() {
        val number = fullPhoneNumber.value
        if (number.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.fragment_auth_phone_error_validation))
            return
        }
        loadingState.onNext(true)
        authRepository.startVerification(number)
    }

    fun verifyPhoneNumberWithCode() {
        val code = phoneCode.value
        if (code.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.fragment_auth_code_error_validation))
            return
        }
        loadingState.onNext(true)
        authRepository.verifyWithCode(code)
    }

    fun resendVerificationCode() {
        val number = fullPhoneNumber.value
        if (number.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.fragment_auth_phone_error_validation))
            return
        }
        authRepository.resendCode(number)
    }

    fun getInitialCountry(): CountryData {
        val countryCode = telephonyManager.getUserCountryCode()
        val country = countryList.find { it.code == countryCode }
        return country ?: CountryData.EMPTY
    }

    private fun setAuthEventListener() {
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
        loadingState.onNext(false)
        val error = when (e) {
            is AuthInvalidCredentialsException -> R.string.fragment_auth_phone_error_validation
            is AuthNotAvailableException -> R.string.fragment_auth_phone_error_too_many_requests
            else -> R.string.common_error
        }
        command.onNext(Events.OnError(error))
    }

    private fun onCodeSent() {
        loadingState.onNext(false)
        command.onNext(Events.OnCodeSent(fullPhoneNumber.requireValue.getFormattedNumber(countryList)))
    }

    private fun onSuccess() {
        userRepository.isUserWithUidExist()
            .subscribe({
                loadingState.onNext(false)
                if (it) {
                    prefsManager.isUserAddInfo = true
                }
                command.onNext(Events.OnComplete(it))
            }, {
                loadingState.onNext(false)
            })
            .addTo(compositeDisposable)
    }

    private fun onIncorrectCode() {
        command.onNext(Events.OnError(R.string.fragment_auth_code_error_validation))
        loadingState.onNext(false)
    }

    sealed class Events {
        class OnCodeSent(val number: String) : Events()
        class OnComplete(val isUserExist: Boolean) : Events()
        class OnError(@StringRes val id: Int) : Events()
    }
}