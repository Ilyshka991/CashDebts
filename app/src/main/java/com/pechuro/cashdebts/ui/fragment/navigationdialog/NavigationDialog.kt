package com.pechuro.cashdebts.ui.fragment.navigationdialog

import android.os.Bundle
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseBottomSheetDialog
import com.pechuro.cashdebts.ui.utils.EventManager
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
            EventManager.publish(NavigationEvent.openLocalDebts)
            close()
        }
        text_remote_debt.setOnClickListener {
            EventManager.publish(NavigationEvent.openRemoteDebts)
            close()
        }
        text_profile.setOnClickListener {
            EventManager.publish(NavigationEvent.openProfile)
            close()
        }
    }

    companion object {
        const val TAG = "navigation_dialog"

        fun newInstance() = NavigationDialog()
    }
}

