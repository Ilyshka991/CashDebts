package com.pechuro.cashdebts.ui.activity.adddebt.localuser

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.LocalDebtInfo
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.utils.binding.receiveDebtRoleChangesFrom
import com.pechuro.cashdebts.ui.utils.binding.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_debt_local_user.*
import kotlinx.android.synthetic.main.layout_debt_role_chooser.*

class AddDebtLocalUserFragment : BaseFragment<AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_local_user
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {
        viewModel.debt.debtRole.receiveDebtRoleChangesFrom(chip_container).addTo(strongCompositeDisposable)
        (viewModel.debt as LocalDebtInfo).name.receiveTextChangesFrom(text_name).addTo(strongCompositeDisposable)
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