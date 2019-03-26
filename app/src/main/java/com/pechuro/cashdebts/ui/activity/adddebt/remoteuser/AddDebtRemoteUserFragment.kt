package com.pechuro.cashdebts.ui.activity.adddebt.remoteuser

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.progressdialog.ProgressDialog
import com.pechuro.cashdebts.ui.utils.transaction
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_debt_remote_user.*

class AddDebtRemoteUserFragment : BaseFragment<AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_remote_user
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REQUEST_PICK_CONTACT && resultCode == RESULT_OK -> {
                data?.data?.let { getContact(it) }
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setListeners() {
        button_pick_contact.setOnClickListener {
            startPickContactActivity()
        }
    }

    private fun setViewModelListeners() {
        viewModel.command.subscribe {
            when (it) {
                is AddDebtActivityViewModel.Events.ShowProgress -> showProgressDialog()
                is AddDebtActivityViewModel.Events.DismissProgress -> dismissProgressDialog()
                is AddDebtActivityViewModel.Events.ShowSnackBarUserNotExist -> showSnackBarUserNotExist()
            }
        }.addTo(weakCompositeDisposable)
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

    private fun getContact(uri: Uri) {
        context?.contentResolver?.query(uri, null, null, null, null).use {
            if (it?.moveToFirst() == true) {
                val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                viewModel.setPhoneData(number)
            }
        }
    }

    private fun showProgressDialog() {
        childFragmentManager.transaction {
            add(ProgressDialog.newInstance(), ProgressDialog.TAG)
            addToBackStack(ProgressDialog.TAG)
        }
    }

    private fun dismissProgressDialog() {
        childFragmentManager.popBackStack()
    }

    companion object {
        const val TAG = "AddDebtRemoteUserFragment"

        private const val REQUEST_PICK_CONTACT = 342

        fun newInstance() = AddDebtRemoteUserFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}