package com.pechuro.cashdebts.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.view.View.VISIBLE
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.auth.code.AuthCodeFragment
import com.pechuro.cashdebts.ui.activity.auth.phone.AuthPhoneFragment
import com.pechuro.cashdebts.ui.activity.base.FragmentSwitcherBaseActivity
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.activity.main.MainActivity


class AuthActivity : FragmentSwitcherBaseActivity<AuthActivityViewModel>() {

    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AuthActivityViewModel::class.java)
    override val homeFragment: Fragment
        get() = AuthPhoneFragment.newInstance()
    override val isCloseButtonEnabled: Boolean
        get() = false
    override val label: Int
        get() = R.string.label_auth_activity

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    override fun onDoneButtonClick(currentPosition: Int) {
        when (currentPosition) {
            0 -> viewModel.startPhoneNumberVerification()
            1 -> viewModel.verifyPhoneNumberWithCode()
        }
    }

    private fun subscribeToEvents() {
        viewModel.command.subscribe {
            when (it) {
                is Events.OnCodeSent -> showNextFragment(AuthCodeFragment.newInstance())
                is Events.OnSuccess -> openMainActivity()
                is Events.OpenCountrySelection -> openCountrySelection()
                is Events.CloseCountrySelection -> closeCountrySelection()
                is Events.ShowSnackBarError -> showSnackBar(it.id)
            }
        }.let(weakCompositeDisposable::add)
    }


    private fun showSnackBar(@StringRes id: Int) {
        Snackbar.make(viewDataBinding.root, id, Snackbar.LENGTH_LONG).show()
    }

    private fun openCountrySelection() {
        val intent = CountrySelectionActivity.newIntent(this)
        startActivity(intent)
    }

    private fun closeCountrySelection() {
        showPreviousFragment()
        viewDataBinding.buttonDone.visibility = VISIBLE
    }

    private fun openMainActivity() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
