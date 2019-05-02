package com.pechuro.cashdebts.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivity
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.profileedit.ProfileEditActivity
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.localdebtlist.LocalDebtListFragment
import com.pechuro.cashdebts.ui.fragment.navigationdialog.NavigationDialog
import com.pechuro.cashdebts.ui.fragment.navigationdialog.NavigationEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.fragment.profileview.ProfileViewFragment
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.RemoteDebtListFragment
import com.pechuro.cashdebts.ui.utils.EventBus
import com.pechuro.cashdebts.ui.utils.SnackbarManager
import com.pechuro.cashdebts.ui.utils.px
import io.reactivex.rxkotlin.addTo
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
        setNavigationListeners()
        setViewListeners()
        setupSnackbarManager()
    }

    override fun onStart() {
        super.onStart()
        setEventListeners()
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
        bottom_app_bar.setNavigationOnClickListener {
            openNavigation()
        }
    }

    private fun setupSnackbarManager() {
        SnackbarManager.listen {
            Snackbar.make(coordinatorLayout, it.msgId, it.duration).apply {
                anchorView = if (fab.isVisible) fab else bottom_app_bar
                it.action?.let { info -> setAction(info.actionId) { info.callback() } }
                show()
            }
        }.addTo(strongCompositeDisposable)
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
            }
        )
    }

    private fun setNavigationListeners() {
        EventBus.listen(NavigationEvent::class.java).distinctUntilChanged().subscribe {
            when (it) {
                is NavigationEvent.openRemoteDebts -> showRemoteDebts()
                is NavigationEvent.openLocalDebts -> showLocalDebts()
                is NavigationEvent.openProfile -> showProfile()
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun showRemoteDebts() {
        showFragment(RemoteDebtListFragment.newInstance(), false)
    }

    private fun showLocalDebts() {
        showFragment(LocalDebtListFragment.newInstance(), false)
    }

    private fun showProfile() {
        showFragment(ProfileViewFragment.newInstance(), false)

    }

    private fun openNavigation() {
        val navDialog = NavigationDialog.newInstance()
        navDialog.show(supportFragmentManager, NavigationDialog.TAG)
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
