package com.pechuro.cashdebts.ui.activity.auth.code

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAuthCodeBinding
import com.pechuro.cashdebts.model.timer.Timer
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.base.base.BaseFragment

class AuthCodeFragment : BaseFragment<FragmentAuthCodeBinding, AuthActivityViewModel>() {
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = R.layout.fragment_auth_code
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AuthActivityViewModel::class

    private var resendTimer: Timer? = null
    private var resendErrorTimer: Timer? = null

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

        setListeners()
        setupView()
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
        with(viewDataBinding) {
            textCode.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        this@AuthCodeFragment.viewModel.verifyPhoneNumberWithCode()
                        return@setOnEditorActionListener true
                    }
                    false
                }
            }

            if (isCodeResent()) {
                textNotReceive.visibility = GONE
                buttonResend.visibility = GONE
                textTime.visibility = GONE
            }
        }
    }

    private fun setListeners() {
        with(viewDataBinding) {
            buttonResend.setOnClickListener {
                onResendButtonClick()
            }
            buttonCodeConfirm.setOnClickListener {
                this@AuthCodeFragment.viewModel.verifyPhoneNumberWithCode()
            }
        }
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
        with(viewDataBinding) {
            textTime.text = getString(R.string.auth_code_time, time)
            progressTime.progress = ((RESEND_TIMEOUT - time) * 100F / RESEND_TIMEOUT).toInt()
        }
    }

    private fun onResendTimerFinish() {
        with(viewDataBinding) {
            buttonResend.isEnabled = true
            textTime.visibility = GONE
            progressTime.visibility = GONE
        }
    }

    private fun onResendErrorTimerFinish() {
        viewDataBinding.textResendError.visibility = VISIBLE
    }

    private fun onResendButtonClick() {
        startResendErrorTimer()
        viewModel.resendVerificationCode()
        with(viewDataBinding) {
            textNotReceive.visibility = GONE
            buttonResend.visibility = GONE
        }
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