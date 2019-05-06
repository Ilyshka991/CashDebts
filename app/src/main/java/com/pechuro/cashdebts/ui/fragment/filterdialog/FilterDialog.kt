package com.pechuro.cashdebts.ui.fragment.filterdialog

import android.os.Bundle
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseBottomSheetDialog
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.dialog_navigation.*

class FilterDialog : BaseBottomSheetDialog() {

    override val layoutId: Int
        get() = R.layout.dialog_navigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {

    }

    companion object {
        const val TAG = "filter_dialog"

        fun newInstance() = FilterDialog()
    }
}

