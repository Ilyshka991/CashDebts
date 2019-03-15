package com.pechuro.cashdebts.ui.activity.auth.phone

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAuthPhoneBinding
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import com.pechuro.cashdebts.ui.custom.phone.PhoneTextWatcher
import javax.inject.Inject

class AuthPhoneFragment : BaseFragment<FragmentAuthPhoneBinding, AuthActivityViewModel>() {
    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AuthActivityViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = R.layout.fragment_auth_phone

    @Inject
    protected lateinit var countryList: List<CountryData>

    private val phoneTextWatcher = object : PhoneTextWatcher {
        override fun onCodeChanged(code: String?) {
            val country = countryList.findLast { it.phonePrefix == code }
            viewModel.countryData.set(country)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) setCountry()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.textPhone.addListener(phoneTextWatcher)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            INTENT_REQUEST_COUNTRY_SELECT -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val country =
                            data?.getParcelableExtra<CountryData>(CountrySelectionActivity.INTENT_DATA_SELECTED_COUNTRY)
                        viewModel.countryData.set(country)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupView() {
        viewDataBinding.textCountry.setOnClickListener {
            openCountrySelection()
        }
        viewDataBinding.textPhone.onDoneClick = {
            viewModel.startPhoneNumberVerification()
        }
    }

    private fun openCountrySelection() {
        context?.let {
            val intent = CountrySelectionActivity.newIntent(it)
            startActivityForResult(intent, INTENT_REQUEST_COUNTRY_SELECT)
        }
    }

    private fun setCountry() {
        val countryCode = getUserCountryCode()
        val country = countryList.find { it.code == countryCode }
        viewModel.countryData.set(country)
    }

    private fun getUserCountryCode(): String? {
        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountry = tm.simCountryIso
        if (simCountry != null && simCountry.length == 2) {
            return simCountry.toUpperCase()
        } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
            val networkCountry = tm.networkCountryIso
            if (networkCountry != null && networkCountry.length == 2) {
                return networkCountry.toUpperCase()
            }
        }
        return null
    }

    companion object {
        private const val INTENT_REQUEST_COUNTRY_SELECT = 133

        fun newInstance() = AuthPhoneFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}