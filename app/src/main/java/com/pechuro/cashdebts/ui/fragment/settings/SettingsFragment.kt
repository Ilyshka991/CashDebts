package com.pechuro.cashdebts.ui.fragment.settings

import android.os.Bundle
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : BaseFragment<SettingsFragmentViewModel>() {
    @Inject
    protected lateinit var prefsManager: PrefsManager

    override val layoutId: Int
        get() = R.layout.fragment_settings

    override fun getViewModelClass() = SettingsFragmentViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setViewListeners()
    }

    private fun setupView() {
        switch_add_plus.isChecked = prefsManager.settingsAutoAddPlus
    }

    private fun setViewListeners() {
        switch_add_plus.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.settingsAutoAddPlus = isChecked
        }
    }

    companion object {

        fun newInstance() = SettingsFragment()
    }
}

