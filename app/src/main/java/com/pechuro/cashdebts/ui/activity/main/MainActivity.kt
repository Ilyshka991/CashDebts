package com.pechuro.cashdebts.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MenuRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivity
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.profileedit.ProfileEditActivity
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.filterdialog.FilterDialog
import com.pechuro.cashdebts.ui.fragment.localdebtlist.LocalDebtListFragment
import com.pechuro.cashdebts.ui.fragment.navigationdialog.NavigationDialog
import com.pechuro.cashdebts.ui.fragment.navigationdialog.NavigationEvent
import com.pechuro.cashdebts.ui.fragment.navigationdialog.NavigationItems
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.fragment.profileview.ProfileViewFragment
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.RemoteDebtListFragment
import com.pechuro.cashdebts.ui.utils.EventManager
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_bottom_bar.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseFragmentActivity<MainActivityViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_bottom_bar
    override val containerId: Int
        get() = container.id

    private var isBottomNavVisible = false
        set(value) {
            field = value
            bottom_app_bar.isVisible = value
        }
    private var isFabVisible = false
        set(value) {
            field = value
            if (value) fab.show() else fab.hide()
        }
    @MenuRes
    private var currentMenuRes: Int = 0
        set(value) {
            field = value
            if (value == 0) {
                bottom_app_bar.menu.clear()
            } else {
                bottom_app_bar.replaceMenu(value)
            }
        }

    private var lastShownSnackbar: Snackbar? = null

    override fun getViewModelClass() = MainActivityViewModel::class

    override fun getHomeFragment() = RemoteDebtListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreState(savedInstanceState)
        if (savedInstanceState == null) openProfileEditIfNecessary()
        setNavigationListeners()
        setViewListeners()
        setupSnackbarManager()
        setEventListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putBoolean(BUNDLE_IS_BOTTOM_NAV_VISIBLE, isBottomNavVisible)
            putBoolean(BUNDLE_IS_FAB_VISIBLE, isFabVisible)
            putInt(BUNDLE_MENU_RES, currentMenuRes)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
    }

    override fun homeFragment() {
        super.homeFragment()
        currentMenuRes = R.menu.menu_debt_list
        isFabVisible = true
        isBottomNavVisible = true
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            isBottomNavVisible = getBoolean(BUNDLE_IS_BOTTOM_NAV_VISIBLE, true)
            isFabVisible = getBoolean(BUNDLE_IS_FAB_VISIBLE, true)
            currentMenuRes = getInt(BUNDLE_MENU_RES, R.menu.menu_activity_add)
        }
    }

    private fun setViewListeners() {
        bottom_app_bar.setNavigationOnClickListener {
            openNavigation()
        }
        bottom_app_bar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_profile_action_edit -> openEditProfile()
                R.id.menu_profile_action_logout -> logout()
                R.id.menu_debt_list_action_filter -> openFilter()
            }
            true
        }
        fab.setOnClickListener {
            when (supportFragmentManager.findFragmentById(containerId)) {
                is RemoteDebtListFragment -> openAddActivity(false)
                is LocalDebtListFragment -> openAddActivity(true)
            }
        }
        nestedScrollView.setOnScrollChangeListener { _: NestedScrollView, _, scrollY, _, oldScrollY ->
            bottom_app_bar.fabAlignmentMode = if (scrollY > oldScrollY)
                BottomAppBar.FAB_ALIGNMENT_MODE_CENTER else BottomAppBar.FAB_ALIGNMENT_MODE_END
        }
    }

    private fun setupSnackbarManager() {
        SnackbarManager.listen {
            if (it.isEmpty()) {
                lastShownSnackbar?.dismiss()
            } else {
                lastShownSnackbar = showSnackbar(it)
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun setEventListeners() {
        EventManager.listen(MainActivityEvent::class.java).subscribe {
            when (it) {
                is MainActivityEvent.OpenAddActivity -> openAddActivity(it.isLocalDebt, it.id)
                is MainActivityEvent.UpdateTotalDebtSum -> updateTotalDebtSum(it.value)
            }
        }.addTo(strongCompositeDisposable)
        EventManager.listen(ProfileEditEvent::class.java).subscribe {
            when (it) {
                is ProfileEditEvent.OnSaved -> {
                    homeFragment()
                    isBottomNavVisible = true
                }
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun setNavigationListeners() {
        EventManager.listen(NavigationEvent::class.java).distinctUntilChanged().subscribe {
            when (it) {
                is NavigationEvent.OpenRemoteDebts -> showRemoteDebts()
                is NavigationEvent.OpenLocalDebts -> showLocalDebts()
                is NavigationEvent.OpenProfile -> showProfile()
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun showSnackbar(info: SnackInfo) = Snackbar
        .make(coordinatorLayout, info.msgId, info.duration).apply {
            setActionTextColor(ResourcesCompat.getColor(resources, R.color.colorOrange, theme))
            anchorView = if (fab.isVisible) fab else bottom_app_bar
            info.action?.let { info -> setAction(info.actionId) { info.callback() } }
            show()
        }

    private fun showRemoteDebts() {
        currentMenuRes = R.menu.menu_debt_list
        isFabVisible = true
        showFragment(RemoteDebtListFragment.newInstance(), false)
    }

    private fun showLocalDebts() {
        currentMenuRes = R.menu.menu_debt_list
        isFabVisible = true
        showFragment(LocalDebtListFragment.newInstance(), false)
    }

    private fun showProfile() {
        showFragment(ProfileViewFragment.newInstance(), false)
        currentMenuRes = R.menu.menu_profile
        isFabVisible = false
    }

    private fun openNavigation() {
        val currentFragment = when (supportFragmentManager.findFragmentById(containerId)) {
            is RemoteDebtListFragment -> NavigationItems.REMOTE_DEBT
            is LocalDebtListFragment -> NavigationItems.LOCAL_DEBT
            is ProfileViewFragment -> NavigationItems.PROFILE
            else -> throw IllegalStateException()
        }
        NavigationDialog.newInstance(currentFragment).apply {
            show(supportFragmentManager, NavigationDialog.TAG)
        }
    }

    private fun openFilter() {
        FilterDialog.newInstance().apply {
            show(supportFragmentManager, FilterDialog.TAG)
        }
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

    private fun openAddActivity(isLocalDebt: Boolean, id: String? = null) {
        val intent = AddDebtActivity.newIntent(this, isLocalDebt, id)
        startActivity(intent)
    }

    private fun updateTotalDebtSum(value: Double) {
        bottom_app_bar.menu.findItem(R.id.menu_debt_list_msg_total)?.apply {
            title = getString(R.string.menu_debt_list_total, value)
            isVisible = true
        }
    }

    private fun openProfileEditIfNecessary() {
        if (!viewModel.isUserAddInfo()) {
            isBottomNavVisible = false
            isFabVisible = false
            showFragment(ProfileEditFragment.newInstance(true), false)
        }
    }

    companion object {
        private const val BUNDLE_IS_BOTTOM_NAV_VISIBLE = "isBottomNavVisible"
        private const val BUNDLE_IS_FAB_VISIBLE = "isFabVisible"
        private const val BUNDLE_MENU_RES = "menuRes"

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
