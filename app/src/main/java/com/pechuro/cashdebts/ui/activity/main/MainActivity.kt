package com.pechuro.cashdebts.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.base.ContainerBaseActivity
import com.pechuro.cashdebts.ui.fragment.debtlist.DebtListFragment
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.rxkotlin.addTo

class MainActivity : ContainerBaseActivity<MainActivityViewModel>() {

    override val viewModel: MainActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    override val homeFragment: Fragment
        get() = DebtListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (!viewModel.isUserAddInfo()) {
                showFragment(ProfileEditFragment.newInstance(true), false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        subscribeToEvents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_action_logout -> {
                logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        viewModel.logout()
        val intent = AuthActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun subscribeToEvents() {
        EventBus.listen(MainActivityEvent::class.java).subscribe {
            when (it) {
                is MainActivityEvent.OpenAddActivity -> openAddActivity()
            }
        }.addTo(weakCompositeDisposable)
        EventBus.listen(ProfileEditEvent::class.java).subscribe {
            when (it) {
                is ProfileEditEvent.OnSaved -> homeFragment()
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun openAddActivity() {
        /*val intent = AddDebtActivity.newIntent(this)
        startActivity(intent)*/
        //showFragment(ProfileEditFragment.newInstance())
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
