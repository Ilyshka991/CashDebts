package com.pechuro.cashdebts.ui.fragment.profileview

import android.os.Bundle
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentProfileViewBinding
import com.pechuro.cashdebts.ui.base.base.BaseFragment

class ProfileViewFragment : BaseFragment<FragmentProfileViewBinding, ProfileViewFragmentViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_profile_view
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    override fun getViewModelClass() = ProfileViewFragmentViewModel::class

    companion object {
        fun newInstance() = ProfileViewFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}