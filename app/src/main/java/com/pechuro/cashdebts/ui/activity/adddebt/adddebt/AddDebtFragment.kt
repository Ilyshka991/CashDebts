package com.pechuro.cashdebts.ui.activity.adddebt.adddebt

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentAddDebtBinding
import com.pechuro.cashdebts.ui.base.BaseFragment

class AddDebtFragment : BaseFragment<FragmentAddDebtBinding, AddDebtFragmentViewModel>() {
    override val viewModel: AddDebtFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AddDebtFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_add_debt

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

    private fun setListeners() {
        viewDataBinding.buttonPickContact.setOnClickListener {
            startPickContactActivity()
        }
        viewDataBinding.buttonSave.setOnClickListener {

            /* FirebaseFirestore.getInstance().collection("debts").add(
                 FirestoreDebt(
                     FirebaseAuth.getInstance().currentUser!!.phoneNumber!!,
                     viewDataBinding.textPhone.text.toString(),
                     viewDataBinding.textValue.text.toString().toDouble(),
                     ""
                 )
             )*/
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
                viewDataBinding.textPhone.setText(number)
                viewDataBinding.textName.setText(name)
            }
        }
    }

    companion object {
        const val TAG = "AddFragment"

        private const val REQUEST_PICK_CONTACT = 342

        fun newInstance() = AddDebtFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}