package com.pechuro.cashdebts.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityBottomNavigationBinding
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.base.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.debtlist.DebtListFragment
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.rxkotlin.addTo

class MainActivity : BaseFragmentActivity<ActivityBottomNavigationBinding, MainActivityViewModel>() {

    override val homeFragment: Fragment
        get() = DebtListFragment.newInstance()
    override val layoutId: Int
        get() = R.layout.activity_bottom_navigation
    override val containerId: Int
        get() = viewDataBinding.container.id

    override fun getViewModelClass() = MainActivityViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) openProfileEditIfNecessary()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
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


    private fun setListeners() {
        viewDataBinding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_nav_remote_debt -> showFragment(DebtListFragment.newInstance(), false)
                R.id.menu_nav_local_debt -> showFragment(DebtListFragment.newInstance(), false)
                R.id.menu_nav_profile -> showFragment(ProfileEditFragment.newInstance(), false)
            }
            true
        }
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

    private fun logout() {
        viewModel.logout()
        val intent = AuthActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun openAddActivity() {
        /*val intent = AddDebtActivity.newIntent(this)
        startActivity(intent)*/
        //showFragment(ProfileEditFragment.newInstance())
    }

    private fun openProfileEditIfNecessary() {
        if (!viewModel.isUserAddInfo()) {
            showFragment(ProfileEditFragment.newInstance(true), false)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
