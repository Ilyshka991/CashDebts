package com.pechuro.cashdebts.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivity
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.profileedit.ProfileEditActivity
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.localdebtlist.LocalDebtListFragment
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.fragment.profileview.ProfileViewEvent
import com.pechuro.cashdebts.ui.fragment.profileview.ProfileViewFragment
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.RemoteDebtListFragment
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseFragmentActivity<MainActivityViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_bottom_navigation
    override val containerId: Int
        get() = container.id

    private var isBottomNavVisible = true
        set(value) {
            field = value
            bottom_app_bar.isVisible = value
            fab.isVisible = value
        }

    override fun getViewModelClass() = MainActivityViewModel::class

    override fun getHomeFragment() = RemoteDebtListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) openProfileEditIfNecessary()
        setViewListeners()
    }

    override fun onStart() {
        super.onStart()
        setEventListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BUNDLE_IS_BOTTOM_NAV_VISIBLE, isBottomNavVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.getBoolean(BUNDLE_IS_BOTTOM_NAV_VISIBLE)?.let {
            isBottomNavVisible = it
        }
    }

    private fun setViewListeners() {
        /*  bottom_navigation.setOnNavigationItemSelectedListener {
              if (bottom_navigation.selectedItemId == it.itemId) {
                  return@setOnNavigationItemSelectedListener true
              }
              when (it.itemId) {
                  R.id.menu_nav_remote_debt -> showFragment(RemoteDebtListFragment.newInstance(), false)
                  R.id.menu_nav_local_debt -> showFragment(LocalDebtListFragment.newInstance(), false)
                  R.id.menu_nav_profile -> showFragment(ProfileViewFragment.newInstance(), false)
              }
              true
          }*/
    }

    private fun setEventListeners() {
        weakCompositeDisposable.addAll(
            EventBus.listen(MainActivityEvent::class.java).subscribe {
                when (it) {
                    is MainActivityEvent.OpenAddActivity -> openAddActivity(it.isLocalDebt, it.id)
                }
            },
            EventBus.listen(ProfileEditEvent::class.java).subscribe {
                when (it) {
                    is ProfileEditEvent.OnSaved -> {
                        homeFragment()
                        isBottomNavVisible = true
                    }
                }
            },
            EventBus.listen(ProfileViewEvent::class.java).subscribe {
                when (it) {
                    is ProfileViewEvent.OnLogout -> logout()
                    is ProfileViewEvent.OpenEditProfile -> openEditProfile()
                }
            }
        )
    }

    private fun openEditProfile() {
        val intent = ProfileEditActivity.newIntent(this)
        startActivity(intent)
    }

    private fun logout() {
        viewModel.logout()
        val intent = AuthActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun openAddActivity(isLocalDebt: Boolean, id: String?) {
        val intent = AddDebtActivity.newIntent(this, isLocalDebt, id)
        startActivity(intent)
    }

    private fun openProfileEditIfNecessary() {
        if (!viewModel.isUserAddInfo()) {
            isBottomNavVisible = false
            showFragment(ProfileEditFragment.newInstance(true), false)
        }
    }

    companion object {
        private const val BUNDLE_IS_BOTTOM_NAV_VISIBLE = "isBottomNavVisible"

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
