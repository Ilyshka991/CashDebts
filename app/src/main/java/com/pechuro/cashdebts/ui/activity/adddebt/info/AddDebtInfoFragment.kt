package com.pechuro.cashdebts.ui.activity.adddebt.info

import android.os.Bundle
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAddDebtInfoBinding
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.progressdialog.ProgressDialog
import com.pechuro.cashdebts.ui.utils.transaction
import io.reactivex.rxkotlin.addTo

class AddDebtInfoFragment : BaseFragment<FragmentAddDebtInfoBinding, AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_info
    override val isViewModelShared: Boolean
        get() = true
    override val bindingVariables: Map<Int, Any>?
        get() = mapOf(BR.viewModel to viewModel)

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    private fun setViewModelListeners() {
        viewModel.command.subscribe {
            when (it) {
                is AddDebtActivityViewModel.Events.ShowProgress -> showProgressDialog()
                is AddDebtActivityViewModel.Events.DismissProgress -> dismissProgressDialog()
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun showProgressDialog() {
        childFragmentManager.transaction {
            add(ProgressDialog.newInstance(), ProgressDialog.TAG)
            addToBackStack(ProgressDialog.TAG)
        }
    }

    private fun dismissProgressDialog() {
        childFragmentManager.popBackStack()
    }

    companion object {
        const val TAG = "AddDebtInfoFragment"

        fun newInstance() = AddDebtInfoFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}