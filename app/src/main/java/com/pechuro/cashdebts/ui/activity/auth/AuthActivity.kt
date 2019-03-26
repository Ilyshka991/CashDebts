package com.pechuro.cashdebts.ui.activity.auth

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.ui.activity.auth.code.AuthCodeFragment
import com.pechuro.cashdebts.ui.activity.auth.phone.AuthPhoneFragment
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import com.pechuro.cashdebts.ui.base.FragmentSwitcherBaseActivity
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_container.*

class AuthActivity : FragmentSwitcherBaseActivity<AuthActivityViewModel>() {
    override val isCloseButtonEnabled: Boolean
        get() = false

    override fun getViewModelClass() = AuthActivityViewModel::class

    override fun getHomeFragment() = AuthPhoneFragment.newInstance()

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModel.command.subscribe {
            when (it) {
                is AuthActivityViewModel.Events.OnCodeSent -> showFragment(AuthCodeFragment.newInstance())
                is AuthActivityViewModel.Events.OnComplete -> openMainActivity()
                is AuthActivityViewModel.Events.OpenProfileEdit -> {
                    isBackAllowed = false
                    showFragment(ProfileEditFragment.newInstance(true))
                }
                is AuthActivityViewModel.Events.ShowSnackBarError -> showSnackBar(it.id)
            }
        }.addTo(weakCompositeDisposable)

        EventBus.listen(ProfileEditEvent::class.java).subscribe {
            when (it) {
                is ProfileEditEvent.OnSaved -> openMainActivity()
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun showSnackBar(@StringRes id: Int) {
        Snackbar.make(container, id, Snackbar.LENGTH_LONG).show()
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
