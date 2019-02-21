package com.pechuro.cashdebts.ui.activity.auth.phone

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAuthPhoneBinding
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment


class AuthPhoneFragment : BaseFragment<FragmentAuthPhoneBinding, AuthActivityViewModel>() {
    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AuthActivityViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = R.layout.fragment_auth_phone

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        viewDataBinding.textPhone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                viewDataBinding.buttonSendCode.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    companion object {

        fun newInstance() = AuthPhoneFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}