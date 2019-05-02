package com.pechuro.cashdebts.ui.fragment.navigationdialog

import android.os.Bundle
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseBottomSheetDialog
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.dialog_navigation.*

class NavigationDialog : BaseBottomSheetDialog() {

    override val layoutId: Int
        get() = R.layout.dialog_navigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {
        text_local_debt.setOnClickListener {
            EventBus.publish(NavigationEvent.openLocalDebts)
        }
        text_remote_debt.setOnClickListener {
            EventBus.publish(NavigationEvent.openRemoteDebts)
        }
        text_profile.setOnClickListener {
            EventBus.publish(NavigationEvent.openProfile)
        }
    }

    companion object {
        const val TAG = "navigation_dialog"

        fun newInstance() = NavigationDialog()
    }
}

