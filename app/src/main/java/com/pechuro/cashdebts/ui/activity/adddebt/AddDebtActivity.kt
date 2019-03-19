package com.pechuro.cashdebts.ui.activity.adddebt

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.ui.activity.adddebt.user.AddDebtUserFragment
import com.pechuro.cashdebts.ui.base.FragmentSwitcherBaseActivity

class AddDebtActivity : FragmentSwitcherBaseActivity<AddDebtActivityViewModel>() {

    override val homeFragment: Fragment
        get() = AddDebtUserFragment.newInstance()
    override val isCloseButtonEnabled: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    /*  override fun onDoneButtonClick(currentPosition: Int) {
          when (currentPosition) {
              0 -> showNextFragment(AddDebtInfoFragment.newInstance())
              1 -> viewModel.save()
          }
      }*/

    private fun subscribeToEvents() {
        viewModel.command.subscribe {
            when (it) {
                is Events.OnSaved -> closeActivity()
                is Events.ShowSnackBarError -> showSnackBar(it.id)
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
        fun newIntent(context: Context) = Intent(context, AddDebtActivity::class.java)
    }
}
