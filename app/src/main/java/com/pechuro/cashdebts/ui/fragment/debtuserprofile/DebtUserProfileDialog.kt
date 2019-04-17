package com.pechuro.cashdebts.ui.fragment.debtuserprofile

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseDialog
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt

class DebtUserProfileDialog : BaseDialog<DebtUserProfileDialogViewModel>() {
    override val layoutId: Int
        get() = R.layout.dialog_debt_user_profile

    override fun getViewModelClass() = DebtUserProfileDialogViewModel::class


    companion object {
        const val TAG = "DebtUserProfileDialog"

        private const val ARG_USER = "argUser"

        fun newInstance(user: RemoteDebt.User) = DebtUserProfileDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }
}