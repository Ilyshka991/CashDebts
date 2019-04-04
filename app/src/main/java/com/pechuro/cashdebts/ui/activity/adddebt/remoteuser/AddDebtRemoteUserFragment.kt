package com.pechuro.cashdebts.ui.activity.adddebt.remoteuser

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.RemoteDebtInfo
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.phone.receiveTextChangesFrom
import com.pechuro.cashdebts.ui.utils.receiveDebtRole
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_debt_remote_user.*
import kotlinx.android.synthetic.main.layout_debt_role_chooser.*

class AddDebtRemoteUserFragment : BaseFragment<AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_remote_user
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REQUEST_PICK_CONTACT && resultCode == RESULT_OK -> {
                data?.data?.let {
                    val number = getContact(it)
                    number?.let {
                        viewModel.setPhoneData(number.replace(Regex("[ -]"), ""))
                        //    text_phone.setText(number)
                    }
                }
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setViewListeners() {
        button_pick_contact.setOnClickListener {
            startPickContactActivity()
        }
        viewModel.debt.debtRole.receiveDebtRole(chip_container)
        text_phone.apply {
            (viewModel.debt as RemoteDebtInfo).phone.receiveTextChangesFrom(text_phone)
        }
    }

    private fun setViewModelListeners() {
        with(viewModel) {
            command.subscribe {
                when (it) {
                    is AddDebtActivityViewModel.Events.ShowProgress -> showProgressDialog()
                    is AddDebtActivityViewModel.Events.DismissProgress -> dismissProgressDialog()
                    is AddDebtActivityViewModel.Events.OnErrorUserNotExist -> showSnackBarUserNotExist()
                }
            }.addTo(weakCompositeDisposable)
            isConnectionAvailable.subscribe {
                onConnectionChanged(it)
            }.addTo(weakCompositeDisposable)
        }
    }

    private fun onConnectionChanged(isAvailable: Boolean) {
        view_no_connection.isVisible = !isAvailable
        viewModel.command.onNext(AddDebtActivityViewModel.Events.SetOptionsMenuEnabled(isAvailable))
    }

    private fun showSnackBarUserNotExist() {
        Snackbar.make(layout_coordinator, R.string.add_debt_error_user_not_exist, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.add_debt_action_add_local) {
                viewModel.restartWithLocalDebtFragment()
            }.show()
    }

    private fun startPickContactActivity() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_CONTACT)
    }

    private fun getContact(uri: Uri): String? {
        context?.contentResolver?.query(uri, null, null, null, null).use {
            if (it?.moveToFirst() == true) {
                return it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
        return null
    }

    companion object {
        private const val REQUEST_PICK_CONTACT = 342

        fun newInstance() = AddDebtRemoteUserFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}