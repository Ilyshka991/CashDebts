package com.pechuro.cashdebts.ui.activity.auth.phone

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.phone.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import javax.inject.Inject

class AuthPhoneFragment : BaseFragment<AuthActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_auth_phone
    override val isViewModelShared: Boolean
        get() = true

    @Inject
    protected lateinit var countryList: List<CountryData>

    override fun getViewModelClass() = AuthActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
        setupView()
        if (savedInstanceState == null) setInitialCountry()
    }

    override fun onStart() {
        super.onStart()
        subscribeToViewModel()
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
            viewModel.fullPhoneNumber.receiveTextChangesFrom(this)
            onDoneClick = viewModel::startPhoneNumberVerification
            onCountryChanged = ::onCountryChanged
        }

        text_country.setOnClickListener {
            openCountrySelectionActivity()
        }
    }

    private fun subscribeToViewModel() {
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
        val countryCode = viewModel.getUserCountryCode()
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