package com.pechuro.cashdebts.ui.activity.auth.phone

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.phone.PhoneTextWatcher
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import javax.inject.Inject

class AuthPhoneFragment : BaseFragment<AuthActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_auth_phone
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AuthActivityViewModel::class

    @Inject
    protected lateinit var countryList: List<CountryData>

    private val phoneTextWatcher = object : PhoneTextWatcher {
        override fun onCodeChanged(code: String?) {
            val country = countryList.findLast { it.phonePrefix == code }
         //   viewModel.countryData.set(country)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) setInitialCountry()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            INTENT_REQUEST_COUNTRY_SELECT -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val country =
                            data?.getParcelableExtra<CountryData>(CountrySelectionActivity.INTENT_DATA_SELECTED_COUNTRY)
                    //    viewModel.countryData.set(country)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setOnClickListeners() {
        button_verify.setOnClickListener {
            this@AuthPhoneFragment.viewModel.startPhoneNumberVerification()
        }

        text_phone.apply {
            addListener(phoneTextWatcher)
            onDoneClick = {
                this@AuthPhoneFragment.viewModel.startPhoneNumberVerification()
            }
        }

        text_country.setOnClickListener {
            openCountrySelectionActivity()
        }
    }

    private fun openCountrySelectionActivity() {
        context?.let {
            val intent = CountrySelectionActivity.newIntent(it)
            startActivityForResult(intent, INTENT_REQUEST_COUNTRY_SELECT)
        }
    }

    private fun setInitialCountry() {
        val countryCode = viewModel.getUserCountryCode()
        val country = countryList.find { it.code == countryCode }
      //  viewModel.countryData.set(country)
    }

    companion object {
        private const val INTENT_REQUEST_COUNTRY_SELECT = 133

        fun newInstance() = AuthPhoneFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}