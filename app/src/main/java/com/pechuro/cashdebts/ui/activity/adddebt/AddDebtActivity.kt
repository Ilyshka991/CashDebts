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
import kotlinx.android.synthetic.main.activity_container.*

class AddDebtActivity : FragmentSwitcherBaseActivity<AddDebtActivityViewModel>() {
    override val isCloseButtonEnabled: Boolean
        get() = true

    private val isLocalDebt: Boolean
        get() = intent.getBooleanExtra(INTENT_EXTRA_IS_LOCAL_DEBT, true)

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
        } else {
            menu?.findItem(R.id.menu_action_save)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_action_next -> viewModel.openInfo()
            R.id.menu_action_save -> viewModel.save()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    private fun setupViewModel() {
        viewModel.setInitialData(isLocalDebt)
    }

    private fun subscribeToEvents() {
        viewModel.command.subscribe {
            when (it) {
                is AddDebtActivityViewModel.Events.OnSaved -> closeActivity()
                is AddDebtActivityViewModel.Events.OnError -> showSnackBarError(it.id)
                is AddDebtActivityViewModel.Events.OpenInfo -> openInfo()
                is AddDebtActivityViewModel.Events.RestartWithLocalDebtFragment -> restartWithLocalDebtFragment()
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun closeActivity() {
        finish()
    }

    private fun openInfo() {
        showFragment(AddDebtInfoFragment.newInstance())
        invalidateOptionsMenu()
    }

    private fun showSnackBarError(@StringRes id: Int) {
        Snackbar.make(container, id, Snackbar.LENGTH_LONG).show()
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

        fun newIntent(context: Context, isLocalDebt: Boolean) =
            Intent(context, AddDebtActivity::class.java).apply {
                putExtra(INTENT_EXTRA_IS_LOCAL_DEBT, isLocalDebt)
            }
    }
}
