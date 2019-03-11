package com.pechuro.cashdebts.ui.fragment.profileview

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentDebtListBinding
import com.pechuro.cashdebts.ui.base.BaseFragment

class ProfileViewFragment : BaseFragment<FragmentDebtListBinding, ProfileViewFragmentViewModel>() {

    override val viewModel: ProfileViewFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_debt_list

    companion object {
        fun newInstance() = ProfileViewFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}