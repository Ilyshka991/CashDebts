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

    private val currentItem: NavigationItems by lazy {
        val index = arguments?.getInt(ARG_CURRENT_ITEM) ?: throw IllegalArgumentException()
        NavigationItems.values().forEach {
            if (index == it.index) {
                return@lazy it
            }
        }
        throw IllegalArgumentException()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setViewListeners()
    }

    private fun setupView() {
        val currentButton = when (currentItem) {
            NavigationItems.REMOTE_DEBT -> button_remote_debt
            NavigationItems.LOCAL_DEBT -> button_local_debt
            NavigationItems.PROFILE -> button_profile
        }
        currentButton.alpha = 0.6f
    }

    private fun setViewListeners() {
        button_local_debt.setOnClickListener {
            EventManager.publish(NavigationEvent.OpenLocalDebts)
            close()
        }
        button_remote_debt.setOnClickListener {
            EventManager.publish(NavigationEvent.OpenRemoteDebts)
            close()
        }
        button_profile.setOnClickListener {
            EventManager.publish(NavigationEvent.OpenProfile)
            close()
        }
    }

    companion object {
        const val TAG = "navigation_dialog"

        private const val ARG_CURRENT_ITEM = "currentItem"

        fun newInstance(currentItem: NavigationItems) = NavigationDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_CURRENT_ITEM, currentItem.index)
            }
        }
    }
}

enum class NavigationItems(val index: Int) {
    REMOTE_DEBT(1), LOCAL_DEBT(2), PROFILE(3)
}
