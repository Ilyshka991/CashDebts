package com.pechuro.cashdebts.ui.activity.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
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
import com.pechuro.cashdebts.ui.activity.settings.SettingsActivity
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
import com.pechuro.cashdebts.ui.fragment.settings.SettingsFragmentEvent
import com.pechuro.cashdebts.ui.utils.EventManager
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_bottom_bar.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.popup_total_sum.view.*

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

    private lateinit var popupTotalSum: PopupWindow

    override fun getViewModelClass() = MainActivityViewModel::class

    override fun getHomeFragment() = RemoteDebtListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreState(savedInstanceState)
        if (savedInstanceState == null) openProfileEditIfNecessary()
        setNavigationListeners()
        setViewListeners()
        setupSnackbarManager()
        setStrongEventListeners()
    }

    override fun onStart() {
        super.onStart()
        setWeakEventListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::popupTotalSum.isInitialized) {
            popupTotalSum.dismiss()
        }
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
        currentMenuRes = R.menu.menu_fragment_debt_list
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
                R.id.menu_profile_action_settings -> openSettingsActivity()
                R.id.menu_debt_list_action_filter -> openFilter()
                R.id.menu_debt_list_msg_total -> showTotalSumPopup()
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
            bottom_app_bar.apply {
                fabAlignmentMode = if (scrollY > oldScrollY) {
                    BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                } else {
                    (behavior as? BottomAppBar.Behavior)?.slideUp(this)
                    BottomAppBar.FAB_ALIGNMENT_MODE_END
                }
            }
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

    private fun setStrongEventListeners() {
        strongCompositeDisposable.addAll(
            EventManager.listen(MainActivityEvent::class.java).subscribe {
                when (it) {
                    is MainActivityEvent.OpenAddActivity -> openAddActivity(it.isLocalDebt, it.id)
                    is MainActivityEvent.UpdateTotalDebtSum -> updateTotalDebtSum(it.value)
                }
            },
            EventManager.listen(SettingsFragmentEvent::class.java).subscribe {
                when (it) {
                    is SettingsFragmentEvent.OnApplyChanges -> recreate()
                }
            }
        )
    }

    private fun setWeakEventListeners() {
        EventManager.listen(ProfileEditEvent::class.java).subscribe {
            when (it) {
                is ProfileEditEvent.OnSaved -> {
                    homeFragment()
                    isBottomNavVisible = true
                    isFabVisible = true
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
            setActionTextColor(ResourcesCompat.getColor(resources, R.color.action_snackbar, theme))
            anchorView = if (fab.isVisible) fab else bottom_app_bar
            info.action?.let { info -> setAction(info.actionId) { info.callback() } }
            show()
        }

    private fun showRemoteDebts() {
        currentMenuRes = R.menu.menu_fragment_debt_list
        isFabVisible = true
        showFragment(RemoteDebtListFragment.newInstance(), false)
    }

    private fun showLocalDebts() {
        currentMenuRes = R.menu.menu_fragment_debt_list
        isFabVisible = true
        showFragment(LocalDebtListFragment.newInstance(), false)
    }

    private fun showProfile() {
        showFragment(ProfileViewFragment.newInstance(), false)
        currentMenuRes = R.menu.menu_fragment_profile
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

    private fun openSettingsActivity() {
        val intent = SettingsActivity.newIntent(this)
        startActivity(intent)
    }

    private fun updateTotalDebtSum(value: Double) {
        popupTotalSum = createTotalSumPopup(value)
        bottom_app_bar.menu.findItem(R.id.menu_debt_list_msg_total)?.isVisible = true
    }

    private fun openProfileEditIfNecessary() {
        if (!viewModel.isUserAddInfo()) {
            isBottomNavVisible = false
            isFabVisible = false
            showFragment(ProfileEditFragment.newInstance(true), false)
        }
    }

    @SuppressLint("InflateParams")
    private fun createTotalSumPopup(value: Double) = PopupWindow(
        layoutInflater.inflate(R.layout.popup_total_sum, null),
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        this.contentView.text_total_sum.text = getString(R.string.popup_total_sum_title, value)
        setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.background_popup,
                theme
            )?.apply {
                setTint(ResourcesCompat.getColor(resources, R.color.background_popup, theme))
            }
        )

        isOutsideTouchable = true
    }

    private fun showTotalSumPopup() {
        val location = IntArray(2)
        val menuView = findViewById<View>(R.id.menu_debt_list_msg_total)
        menuView.getLocationOnScreen(location)

        popupTotalSum.showAtLocation(
            menuView,
            Gravity.NO_GRAVITY,
            location[0],
            location[1] - bottom_app_bar.height
        )
    }

    companion object {
        private const val BUNDLE_IS_BOTTOM_NAV_VISIBLE = "isBottomNavVisible"
        private const val BUNDLE_IS_FAB_VISIBLE = "isFabVisible"
        private const val BUNDLE_MENU_RES = "menuRes"

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
