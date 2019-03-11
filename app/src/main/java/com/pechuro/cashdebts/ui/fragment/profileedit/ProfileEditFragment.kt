package com.pechuro.cashdebts.ui.fragment.profileedit

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentProfileEditBinding
import com.pechuro.cashdebts.ui.base.BaseFragment

class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding, ProfileEditFragmentViewModel>() {

    override val viewModel: ProfileEditFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ProfileEditFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_debt_list
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    companion object {
        fun newInstance() = ProfileEditFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}