package com.pechuro.cashdebts.ui.fragment.profileview

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentProfileViewBinding
import com.pechuro.cashdebts.ui.base.BaseFragment

class ProfileViewFragment : BaseFragment<FragmentProfileViewBinding, ProfileViewFragmentViewModel>() {

    override val viewModel: ProfileViewFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_profile_view
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    companion object {
        fun newInstance() = ProfileViewFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}