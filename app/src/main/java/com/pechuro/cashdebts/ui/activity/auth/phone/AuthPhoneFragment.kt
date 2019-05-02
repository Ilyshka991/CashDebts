package com.pechuro.cashdebts.ui.activity.auth.phone

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.phone.receiveTextChangesFrom
import com.pechuro.cashdebts.ui.utils.getUserCountryCode
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import javax.inject.Inject

class AuthPhoneFragment : BaseFragment<AuthActivityViewModel>() {

    @Inject
    protected lateinit var countryList: List<CountryData>
    @Inject
    protected lateinit var telephonyManager: TelephonyManager

    override val layoutId: Int
        get() = R.layout.fragment_auth_phone
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AuthActivityViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
        setupView()
        if (savedInstanceState == null) setInitialCountry()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            INTENT_REQUEST_COUNTRY_SELECT -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val country =
                            data?.getParcelableExtra<CountryData>(CountrySelectionActivity.INTENT_DATA_SELECTED_COUNTRY)
                        text_phone.countryData = country ?: CountryData.EMPTY
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setViewListeners() {
        button_verify.setOnClickListener {
            viewModel.startPhoneNumberVerification()
        }

        text_phone.apply {
            viewModel.fullPhoneNumber.receiveTextChangesFrom(this).addTo(strongCompositeDisposable)
            onDoneClick = viewModel::startPhoneNumberVerification
            onCountryChanged = ::onCountryChanged
        }

        text_country.setOnClickListener {
            openCountrySelectionActivity()
        }
    }

    private fun setViewModelListeners() {
        viewModel.loadingState.subscribe {
            button_verify.setProgress(it)
        }.addTo(weakCompositeDisposable)
    }

    private fun onCountryChanged(country: CountryData) {
        if (country.isEmpty) {
            text_country.setText(R.string.auth_invalid_country)
        } else {
            text_country.setText(country.name)
        }
    }

    private fun setupView() {
        text_phone.countryList = countryList
    }

    private fun setInitialCountry() {
        text_phone.countryData = getInitialCountry()
    }

    private fun getInitialCountry(): CountryData {
        val countryCode = telephonyManager.getUserCountryCode()
        val country = countryList.find { it.code == countryCode }
        return country ?: CountryData.EMPTY
    }

    private fun openCountrySelectionActivity() {
        context?.let {
            val intent = CountrySelectionActivity.newIntent(it)
            startActivityForResult(intent, INTENT_REQUEST_COUNTRY_SELECT)
        }
    }

    companion object {
        private const val INTENT_REQUEST_COUNTRY_SELECT = 133

        fun newInstance() = AuthPhoneFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}