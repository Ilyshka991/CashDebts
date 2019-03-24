package com.pechuro.cashdebts.ui.activity.adddebt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.ui.activity.adddebt.info.AddDebtInfoFragment
import com.pechuro.cashdebts.ui.activity.adddebt.localuser.AddDebtLocalUserFragment
import com.pechuro.cashdebts.ui.base.FragmentSwitcherBaseActivity

class AddDebtActivity : FragmentSwitcherBaseActivity<AddDebtActivityViewModel>() {
    override val isCloseButtonEnabled: Boolean
        get() = true

    override fun getHomeFragment() = AddDebtLocalUserFragment.newInstance()

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    private fun setupViewModel() {
        val isLocalDebt = intent.getBooleanExtra(INTENT_EXTRA_IS_LOCAL_DEBT, true)
        viewModel.setInitialData(isLocalDebt)
    }

    private fun subscribeToEvents() {
        viewModel.command.subscribe {
            when (it) {
                is AddDebtActivityViewModel.Events.OnSaved -> closeActivity()
                is AddDebtActivityViewModel.Events.ShowSnackBarError -> showSnackBar(it.id)
                is AddDebtActivityViewModel.Events.OpenInfo -> showFragment(AddDebtInfoFragment.newInstance())
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun closeActivity() {
        finish()
    }

    private fun showSnackBar(@StringRes id: Int) {
        Snackbar.make(viewDataBinding.root, id, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val INTENT_EXTRA_IS_LOCAL_DEBT = "isLocalDebt"

        fun newIntent(context: Context, isLocalDebt: Boolean) =
            Intent(context, AddDebtActivity::class.java).apply {
                getBooleanExtra(INTENT_EXTRA_IS_LOCAL_DEBT, isLocalDebt)
            }
    }
}
