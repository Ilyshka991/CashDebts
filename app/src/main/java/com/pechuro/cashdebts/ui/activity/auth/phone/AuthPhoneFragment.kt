package com.pechuro.cashdebts.ui.activity.auth.phone

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.widget.phone.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import javax.inject.Inject

class AuthPhoneFragment : BaseFragment<AuthActivityViewModel>() {

    @Inject
    protected lateinit var imm: InputMethodManager

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
                showSoftInput()
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
            text_country.setText(R.string.fragment_auth_phone_text_invalid_country)
        } else {
            text_country.setText(country.name)
        }
    }

    private fun setupView() {
        text_phone.countryList = viewModel.countryList
    }

    private fun showSoftInput() {
        requireView().postDelayed({
            imm.showSoftInput(requireView().findFocus(), 0)
        }, 50)
    }

    private fun setInitialCountry() {
        viewModel.getInitialCountry().also {
            text_phone.countryData = it
            if (it.isEmpty) {
                text_phone.textCode.requestFocus()
            } else {
                text_phone.textNumber.requestFocus()
            }
        }
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