package com.pechuro.cashdebts.ui.activity.auth.code

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.timer.Timer
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.hintedittext.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_auth_code.*

class AuthCodeFragment : BaseFragment<AuthActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_auth_code
    override val isViewModelShared: Boolean
        get() = true

    private var resendTimer: Timer? = null
    private var resendErrorTimer: Timer? = null

    override fun getViewModelClass() = AuthActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val isCodeResent = savedInstanceState?.getBoolean(BUNDLE_IS_CODE_RESENT) ?: false
        if (isCodeResent) {
            val lastTick = savedInstanceState?.getLong(BUNDLE_RESEND_ERROR_TIMER_TICK) ?: RESEND_ERROR_TIMEOUT
            startResendErrorTimer(lastTick)
        } else {
            val lastTick = savedInstanceState?.getLong(BUNDLE_RESEND_TIMER_TICK) ?: RESEND_TIMEOUT
            startResendTimer(lastTick)
        }

        setViewListeners()
        setupView()
    }

    override fun onStart() {
        super.onStart()
        subscribeToViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            resendTimer?.lastTick?.let { putLong(BUNDLE_RESEND_TIMER_TICK, it) }
            resendErrorTimer?.lastTick?.let { putLong(BUNDLE_RESEND_ERROR_TIMER_TICK, it) }
            putBoolean(BUNDLE_IS_CODE_RESENT, isCodeResent())
        }
    }

    private fun setupView() {
        text_code.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    viewModel.verifyPhoneNumberWithCode()
                    return@setOnEditorActionListener true
                }
                false
            }
        }

        if (isCodeResent()) {
            text_not_receive.visibility = GONE
            button_resend.visibility = GONE
            text_time.visibility = GONE
        }
    }

    private fun setViewListeners() {
        button_resend.setOnClickListener {
            onResendButtonClick()
        }
        button_code_confirm.setOnClickListener {
            viewModel.verifyPhoneNumberWithCode()
        }
        viewModel.phoneCode.receiveTextChangesFrom(text_code)
    }

    private fun subscribeToViewModel() {
        viewModel.loadingState.subscribe {
            button_code_confirm.setProgress(it)
        }.addTo(weakCompositeDisposable)
    }

    private fun startResendTimer(startWith: Long = RESEND_TIMEOUT) {
        resendTimer = Timer(RESEND_TIMEOUT, startWith).also {
            it.start(::updateTimeViews, ::onResendTimerFinish)
        }
    }

    private fun startResendErrorTimer(startWith: Long = RESEND_ERROR_TIMEOUT) {
        resendErrorTimer = Timer(RESEND_ERROR_TIMEOUT, startWith).also {
            it.start({}, ::onResendErrorTimerFinish)
        }
    }

    private fun stopTimers() {
        resendTimer?.stop()
        resendErrorTimer?.stop()
    }

    private fun updateTimeViews(time: Long) {
        text_time.text = getString(R.string.auth_code_time, time)
        progress_time.progress = ((RESEND_TIMEOUT - time) * 100F / RESEND_TIMEOUT).toInt()
    }

    private fun onResendTimerFinish() {
        button_resend.isEnabled = true
        text_time.visibility = GONE
        progress_time.visibility = GONE
    }

    private fun onResendErrorTimerFinish() {
        text_resend_error.visibility = VISIBLE
    }

    private fun onResendButtonClick() {
        startResendErrorTimer()
        viewModel.resendVerificationCode()
        text_not_receive.visibility = GONE
        button_resend.visibility = GONE
    }

    private fun isCodeResent() = resendErrorTimer != null

    companion object {
        private const val RESEND_TIMEOUT = 60L
        private const val RESEND_ERROR_TIMEOUT = 10L

        private const val BUNDLE_RESEND_TIMER_TICK = "timer_tick"
        private const val BUNDLE_RESEND_ERROR_TIMER_TICK = "error_timer_tick"
        private const val BUNDLE_IS_CODE_RESENT = "is_code_resent"

        fun newInstance() = AuthCodeFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}