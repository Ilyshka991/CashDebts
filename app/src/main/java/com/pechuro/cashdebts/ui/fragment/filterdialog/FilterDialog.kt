package com.pechuro.cashdebts.ui.fragment.filterdialog

import android.os.Bundle
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseBottomSheetDialog
import com.pechuro.cashdebts.ui.utils.EventManager
import kotlinx.android.synthetic.main.dialog_filter.*
import javax.inject.Inject

class FilterDialog : BaseBottomSheetDialog() {

    @Inject
    protected lateinit var prefsManager: PrefsManager

    override val layoutId: Int
        get() = R.layout.dialog_filter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setViewListeners()
    }

    private fun setupView() {
        switch_unite_persons.isChecked = prefsManager.filterUnitePersons
        switch_show_completed.isChecked = prefsManager.filterNotShowCompleted
    }

    private fun setViewListeners() {
        switch_unite_persons.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.filterUnitePersons = isChecked
            EventManager.publish(FilterEvent.OnChange)
        }
        switch_show_completed.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.filterNotShowCompleted = isChecked
            EventManager.publish(FilterEvent.OnChange)
        }
    }

    companion object {
        const val TAG = "filter_dialog"

        fun newInstance() = FilterDialog()
    }
}

