package com.pechuro.cashdebts.ui.activity.adddebt.user

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.inputmethod.EditorInfo
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAddDebtUserBinding
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.base.BaseFragment

class AddDebtUserFragment : BaseFragment<FragmentAddDebtUserBinding, AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_user
    override val bindingVariables: Map<Int, Any>?
        get() = mapOf(BR.viewModel to viewModel)
    override val isViewModelShared: Boolean
        get() = true

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
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

    private fun setupView() {
        viewDataBinding.textPhone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setListeners() {
        viewDataBinding.buttonPickContact.setOnClickListener {
            startPickContactActivity()
        }
    }

    private fun startPickContactActivity() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_CONTACT)
    }

    private fun getContact(uri: Uri) {
        context?.contentResolver?.query(uri, null, null, null, null).use {
            if (it?.moveToFirst() == true) {
                val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                viewModel.setData(name, number)
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