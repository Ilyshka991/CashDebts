package com.pechuro.cashdebts.ui.activity.adddebt.localuser

import android.os.Bundle
import android.view.inputmethod.EditorInfo
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

        text_name.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                viewModel.openInfo()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    companion object {
        fun newInstance() = AddDebtLocalUserFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}