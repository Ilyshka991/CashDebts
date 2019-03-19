package com.pechuro.cashdebts.ui.activity.adddebt.info

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAddDebtInfoBinding
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.base.BaseFragment

class AddDebtInfoFragment : BaseFragment<FragmentAddDebtInfoBinding, AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_info
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    companion object {
        const val TAG = "AddDebtInfoFragment"

        fun newInstance() = AddDebtInfoFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}