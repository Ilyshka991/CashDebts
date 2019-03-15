package com.pechuro.cashdebts.ui.activity.auth.code

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAuthCodeBinding
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import io.reactivex.rxkotlin.addTo

class AuthCodeFragment : BaseFragment<FragmentAuthCodeBinding, AuthActivityViewModel>() {
    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AuthActivityViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = R.layout.fragment_auth_code

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.timer.subscribe(this::updateTimeViews, {}, this::onTimerFinish).addTo(weakCompositeDisposable)
    }

    private fun setupView() {
        viewDataBinding.textCode.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    viewModel.verifyPhoneNumberWithCode()
                    return@setOnEditorActionListener true
                }
                false
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

    private fun updateTimeViews(time: Long) {
        println("AAAAAAAAAA $time")
        with(viewDataBinding) {
            textTime.text = getString(R.string.auth_code_time, time / 1000)
            progressTime.progress = ((RESEND_TIMEOUT - time) * 100F / RESEND_TIMEOUT).toInt()
        }
    }

    private fun onTimerFinish() {
        with(viewDataBinding) {
            buttonResend.isEnabled = true
            textTime.visibility = GONE
            progressTime.visibility = GONE
        }
    }

    private fun onResendButtonClick() {
        viewModel.resendVerificationCode()
        with(viewDataBinding) {
            textNotReceive.visibility = GONE
            buttonResend.visibility = GONE
            textResendError.postDelayed({
                textResendError.visibility = VISIBLE
            }, RESEND_ERROR)
        }
    }

    companion object {
        private const val RESEND_TIMEOUT = 60 * 1000L
        private const val TIMER_INTERVAL = 1 * 1000L
        private const val RESEND_ERROR = 20 * 1000L

        fun newInstance() = AuthCodeFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}