package com.pechuro.cashdebts.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityAuthBinding
import com.pechuro.cashdebts.ui.activity.auth.code.AuthCodeFragment
import com.pechuro.cashdebts.ui.activity.auth.phone.AuthPhoneFragment
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import com.pechuro.cashdebts.ui.base.BaseActivity
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.transaction

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthActivityViewModel>() {
    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AuthActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        if (savedInstanceState == null) homeFragment()
    }

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    override fun onSupportNavigateUp(): Boolean {
        showPreviousFragment()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            showPreviousFragment()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
    }

    private fun subscribeToEvents() {
        viewModel.command.subscribe {
            when (it) {
                is Events.OnStartVerification -> showNextFragment(AuthCodeFragment.newInstance())
                is Events.OnSuccess -> openNextActivity()
                is Events.ShowSnackBarError -> showSnackBar(it.id)
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun homeFragment() {
        val fragment = AuthPhoneFragment.newInstance()
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, fragment)
        }
    }

    private fun <T : ViewDataBinding, V : BaseViewModel> showNextFragment(fragment: BaseFragment<T, V>) {
        supportFragmentManager.transaction {
            setCustomAnimations(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left,
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right
            )
            replace(viewDataBinding.container.id, fragment)
            addToBackStack(null)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showPreviousFragment() {
        supportFragmentManager.popBackStack()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun showSnackBar(@StringRes id: Int) {
        Snackbar.make(viewDataBinding.root, id, Snackbar.LENGTH_LONG).show()
    }

    private fun openNextActivity() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
