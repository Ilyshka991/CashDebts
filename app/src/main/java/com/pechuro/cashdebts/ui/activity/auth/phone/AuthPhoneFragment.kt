package com.pechuro.cashdebts.ui.activity.auth.phone

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.utils.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_auth_phone.*

class AuthPhoneFragment : BaseFragment<AuthActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_auth_phone
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AuthActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
    }

    override fun onStart() {
        super.onStart()
        subscribeToData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            INTENT_REQUEST_COUNTRY_SELECT -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val country =
                            data?.getParcelableExtra<CountryData>(CountrySelectionActivity.INTENT_DATA_SELECTED_COUNTRY)
                        viewModel.countryData.onNext(country ?: CountryData.EMPTY)
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
            viewModel.phoneCode.receiveTextChangesFrom(textCode)
            onDoneClick = viewModel::startPhoneNumberVerification
        }

        text_country.setOnClickListener {
            openCountrySelectionActivity()
        }
    }

    private fun subscribeToData() {
        viewModel.countryDataObservable.subscribe {
            text_phone.setCountryData(it)
            if (!it.isEmpty) {
                text_country.setText(it.name)
            } else {
                text_country.setText(R.string.auth_invalid_country)
            }
        }.addTo(weakCompositeDisposable)
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