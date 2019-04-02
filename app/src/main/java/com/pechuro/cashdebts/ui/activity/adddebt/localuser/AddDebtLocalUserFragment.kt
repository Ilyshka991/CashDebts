package com.pechuro.cashdebts.ui.activity.adddebt.localuser

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.LocalDebtInfo
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.utils.receiveDebtRole
import com.pechuro.cashdebts.ui.utils.receiveTextChangesFrom
import kotlinx.android.synthetic.main.fragment_add_debt_local_user.*
import kotlinx.android.synthetic.main.layout_debt_role_chooser.*

class AddDebtLocalUserFragment : BaseFragment<AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_local_user
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {
        viewModel.debt.debtRole.receiveDebtRole(chip_container)
        (viewModel.debt as LocalDebtInfo).name.receiveTextChangesFrom(text_name)
    }

    companion object {
        fun newInstance() = AddDebtLocalUserFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}