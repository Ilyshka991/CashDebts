package com.pechuro.cashdebts.ui.activity.auth

import androidx.annotation.StringRes
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val prefsManager: PrefsManager
) : BaseViewModel() {
    val command = PublishSubject.create<Events>()

    val fullPhoneNumber = BehaviorSubject.createDefault("")
    val phoneCode = BehaviorSubject.createDefault("")

    private val _loadingState = BehaviorSubject.createDefault(false)
    val loadingState: Observable<Boolean> = _loadingState

    init {
        setAuthEventListener()
    }

    fun startPhoneNumberVerification() {
        val number = fullPhoneNumber.value
        if (number.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.error_auth_phone_validation))
            return
        }
        _loadingState.onNext(true)
        authRepository.startVerification(number)
    }

    fun verifyPhoneNumberWithCode() {
        val code = phoneCode.value
        if (code.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.error_auth_code_validation))
            return
        }
        _loadingState.onNext(true)
        authRepository.verifyWithCode(code)
    }

    fun resendVerificationCode() {
        val number = fullPhoneNumber.value
        if (number.isNullOrEmpty()) {
            command.onNext(Events.OnError(R.string.error_auth_phone_validation))
            return
        }
        authRepository.resendCode(number)
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

    private fun onError(e: com.pechuro.cashdebts.data.data.exception.AuthException) {
        _loadingState.onNext(false)
        val error = when (e) {
            is com.pechuro.cashdebts.data.data.exception.AuthInvalidCredentialsException -> R.string.error_auth_phone_validation
            is com.pechuro.cashdebts.data.data.exception.AuthNotAvailableException -> R.string.error_auth_too_many_requests
            else -> R.string.error_auth_common
        }
        command.onNext(Events.OnError(error))
    }

    private fun onCodeSent() {
        _loadingState.onNext(false)
        command.onNext(Events.OnCodeSent)
    }

    private fun onSuccess() {
        userRepository.isUserWithUidExist()
            .subscribe({
                _loadingState.onNext(false)
                if (it) {
                    prefsManager.isUserAddInfo = true
                }
                command.onNext(Events.OnComplete(it))
            }, {
                _loadingState.onNext(false)
            })
            .addTo(compositeDisposable)
    }

    private fun onIncorrectCode() {
        command.onNext(Events.OnError(R.string.error_auth_code_validation))
        _loadingState.onNext(false)
    }

    sealed class Events {
        object OnCodeSent : Events()
        class OnComplete(val isUserExist: Boolean) : Events()
        class OnError(@StringRes val id: Int) : Events()
    }
}