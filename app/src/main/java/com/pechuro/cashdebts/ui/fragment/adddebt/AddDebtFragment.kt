package com.pechuro.cashdebts.ui.fragment.adddebt

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAddDebtBinding
import com.pechuro.cashdebts.ui.base.BaseFragment

class AddDebtFragment : BaseFragment<FragmentAddDebtBinding, AddDebtFragmentViewModel>() {
    override val viewModel: AddDebtFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AddDebtFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_add_debt


    companion object {
        fun newInstance() = AddDebtFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}