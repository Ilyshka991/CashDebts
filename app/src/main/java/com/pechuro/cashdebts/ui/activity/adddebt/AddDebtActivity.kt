package com.pechuro.cashdebts.ui.activity.adddebt

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.ui.activity.adddebt.info.AddDebtInfoFragment
import com.pechuro.cashdebts.ui.activity.adddebt.user.AddDebtUserFragment
import com.pechuro.cashdebts.ui.activity.base.FragmentSwitcherBaseActivity
import com.pechuro.cashdebts.ui.utils.transaction

class AddDebtActivity : FragmentSwitcherBaseActivity<AddDebtActivityViewModel>() {

    override val isCloseButtonEnabled: Boolean
        get() = true
    override val viewModel: AddDebtActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AddDebtActivityViewModel::class.java)

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    override fun onDoneButtonClick() {
        showNextFragment {
            replace(viewDataBinding.container.id, AddDebtInfoFragment.newInstance())
        }
    }

    override fun homeFragment() {
        val fragment = AddDebtUserFragment.newInstance()
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, fragment)
        }
    }

    private fun subscribeToEvents() {
        viewModel.command.subscribe {
            when (it) {
                is Events.ShowInfoFragment -> showNextFragment {
                    replace(viewDataBinding.container.id, AddDebtInfoFragment.newInstance())
                }
                //  is Events.ShowUserfragment -> showNextFragment(AddDebtUserFragment.newInstance())
                is Events.ShowSnackBarError -> showSnackBar(it.id)
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun showSnackBar(@StringRes id: Int) {
        Snackbar.make(viewDataBinding.root, id, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AddDebtActivity::class.java)
    }
}
