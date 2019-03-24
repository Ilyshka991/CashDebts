package com.pechuro.cashdebts.ui.activity.adddebt.localuser

import android.os.Bundle
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAddDebtLocalUserBinding
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.base.BaseFragment

class AddDebtLocalUserFragment : BaseFragment<FragmentAddDebtLocalUserBinding, AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_local_user
    override val bindingVariables: Map<Int, Any>?
        get() = mapOf(BR.viewModel to viewModel)
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    companion object {
        const val TAG = "AddDebtLocalUserFragment"

        fun newInstance() = AddDebtLocalUserFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}