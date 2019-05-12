package com.pechuro.cashdebts.ui.fragment.settings

import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragment

class SettingsFragment : BaseFragment<SettingsFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings

    override fun getViewModelClass() = SettingsFragmentViewModel::class

    companion object {

        fun newInstance() = SettingsFragment()
    }
}

