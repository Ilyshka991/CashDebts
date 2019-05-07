package com.pechuro.cashdebts.ui.fragment.debtuserprofiledialog

import android.os.Bundle
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseDialog
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt
import com.pechuro.cashdebts.ui.utils.extensions.loadAvatar
import kotlinx.android.synthetic.main.dialog_debt_user_profile.*

class DebtUserProfileDialog : BaseDialog<DebtUserProfileDialogViewModel>() {
    override val layoutId: Int
        get() = R.layout.dialog_debt_user_profile

    private val user: RemoteDebt.User by lazy {
        arguments?.getParcelable<RemoteDebt.User>(ARG_USER) ?: throw RuntimeException()
    }

    override fun getViewModelClass() = DebtUserProfileDialogViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        with(user) {
            image_avatar.loadAvatar(photoUrl)
            text_first_name.text = firstName
            text_last_name.text = lastName
            text_phone.text = phoneNumber
        }
    }

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