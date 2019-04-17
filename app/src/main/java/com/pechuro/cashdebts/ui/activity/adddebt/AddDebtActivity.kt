package com.pechuro.cashdebts.ui.activity.adddebt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.info.AddDebtInfoFragment
import com.pechuro.cashdebts.ui.activity.adddebt.localuser.AddDebtLocalUserFragment
import com.pechuro.cashdebts.ui.activity.adddebt.remoteuser.AddDebtRemoteUserFragment
import com.pechuro.cashdebts.ui.base.activity.FragmentSwitcherBaseActivity
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.fragment_add_debt_remote_user.*

class AddDebtActivity : FragmentSwitcherBaseActivity<AddDebtActivityViewModel>() {
    override val isCloseButtonEnabled: Boolean
        get() = true

    private val isLocalDebt: Boolean
        get() = intent.getBooleanExtra(INTENT_EXTRA_IS_LOCAL_DEBT, true)
    private val debtId: String?
        get() = intent.getStringExtra(INTENT_EXTRA_DEBT_ID)

    private var isOptionsMenuEnabled = true
        set(value) {
            field = value
            invalidateOptionsMenu()
        }

    override fun getHomeFragment() =
        if (isLocalDebt) AddDebtLocalUserFragment.newInstance() else AddDebtRemoteUserFragment.newInstance()

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_add, menu)
        if (backStackSize > 0) {
            menu?.findItem(R.id.menu_action_next)?.isVisible = false
            menu?.findItem(R.id.menu_action_save)?.isEnabled = isOptionsMenuEnabled
        } else {
            menu?.findItem(R.id.menu_action_save)?.isVisible = false
            menu?.findItem(R.id.menu_action_next)?.isEnabled = isOptionsMenuEnabled
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_action_next -> viewModel.validatePersonInfo()
            R.id.menu_action_save -> viewModel.save()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        setViewModelEventListener()
    }

    private fun setupViewModel() {
        viewModel.setInitialData(isLocalDebt)
        debtId?.let { viewModel.loadExistingDebt(it) }
    }

    private fun setViewModelEventListener() {
        viewModel.command.subscribe {
            when (it) {
                is AddDebtActivityViewModel.Events.OnSaved -> onFinish()
                is AddDebtActivityViewModel.Events.OnError -> showSnackBarError(it.id)
                is AddDebtActivityViewModel.Events.OpenInfo -> openInfo(it.isInternetRequired)
                is AddDebtActivityViewModel.Events.OnUserNotExist -> showSnackBarUserNotExist()
                is AddDebtActivityViewModel.Events.SetOptionsMenuEnabled -> setOptionMenuEnabled(it.isEnabled)
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun onFinish() {
        finish()
        EventBus.publish(AddDebtEvent.OnSuccess)
    }

    private fun setOptionMenuEnabled(isEnabled: Boolean) {
        isOptionsMenuEnabled = isEnabled
    }

    private fun openInfo(isInternetRequired: Boolean) {
        showFragment(AddDebtInfoFragment.newInstance(isInternetRequired))
        invalidateOptionsMenu()
    }

    private fun showSnackBarError(@StringRes id: Int) {
        Snackbar.make(container, id, Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackBarUserNotExist() {
        Snackbar.make(layout_coordinator, R.string.add_debt_error_user_not_exist, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.add_debt_action_add_local) {
                restartWithLocalDebtFragment()
            }.show()
    }

    private fun restartWithLocalDebtFragment() {
        intent.apply {
            putExtra(INTENT_EXTRA_IS_LOCAL_DEBT, true)
        }
        finish()
        startActivity(intent)
    }

    companion object {
        private const val INTENT_EXTRA_IS_LOCAL_DEBT = "isLocalDebt"
        private const val INTENT_EXTRA_DEBT_ID = "debtId"

        fun newIntent(context: Context, isLocalDebt: Boolean, id: String?) =
            Intent(context, AddDebtActivity::class.java).apply {
                putExtra(INTENT_EXTRA_IS_LOCAL_DEBT, isLocalDebt)
                id?.let { putExtra(INTENT_EXTRA_DEBT_ID, it) }
            }
    }
}
