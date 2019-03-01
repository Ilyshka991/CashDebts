package com.pechuro.cashdebts.ui.activity.adddebt.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAddDebtUserBinding
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment

class AddDebtUserFragment : BaseFragment<FragmentAddDebtUserBinding, AddDebtActivityViewModel>() {
    override val viewModel: AddDebtActivityViewModel
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AddDebtActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_user

    private fun startPickContactActivity() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_CONTACT)
    }

    private fun getContact(uri: Uri) {
        context?.contentResolver?.query(uri, null, null, null, null).use {
            if (it?.moveToFirst() == true) {
                val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                viewDataBinding.textPhone.setText(number)
                viewDataBinding.textName.setText(name)
            }
        }
    }

    companion object {
        const val TAG = "AddDebtUserFragment"

        private const val REQUEST_PICK_CONTACT = 342

        fun newInstance() = AddDebtUserFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}