package com.pechuro.cashdebts.ui.activity.adddebt.remoteuser

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.activity.adddebt.model.impl.RemoteDebtInfo
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.phone.receiveTextChangesFrom
import com.pechuro.cashdebts.ui.utils.binding.receiveDebtRoleChangesFrom
import com.pechuro.cashdebts.ui.utils.getUserCountryCode
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_debt_remote_user.*
import kotlinx.android.synthetic.main.layout_debt_role_chooser.*
import javax.inject.Inject


class AddDebtRemoteUserFragment : BaseFragment<AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_remote_user
    override val isViewModelShared: Boolean
        get() = true

    @Inject
    protected lateinit var telephonyManager: TelephonyManager
    @Inject
    protected lateinit var countryList: List<CountryData>

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
        setupView()
        if (savedInstanceState == null) setInitialCountry()
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
                    if (number.isNullOrEmpty()) {
                        showSnackBarError(R.string.add_debt_invalid_number)
                        return
                    }
                    val formattedNumber = number.replace(Regex("[ -]"), "")
                    if (text_phone.setPhoneNumber(formattedNumber)) {
                        viewModel.setPhoneData(formattedNumber)
                    } else {
                        showSnackBarError(R.string.add_debt_invalid_number)
                    }
                }
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupView() {
        text_phone.apply {
            onDoneClick = viewModel::validatePersonInfo
            countryList = this@AddDebtRemoteUserFragment.countryList
            textNumber.requestFocus()
        }
    }

    private fun setViewListeners() {
        button_pick_contact.setOnClickListener {
            startPickContactActivity()
        }
        viewModel.debt.debtRole.receiveDebtRoleChangesFrom(chip_container).addTo(strongCompositeDisposable)
        (viewModel.debt as RemoteDebtInfo).phone.receiveTextChangesFrom(text_phone).addTo(strongCompositeDisposable)
    }

    private fun setViewModelListeners() {
        with(viewModel) {
            loadingState.subscribe {
                when (it) {
                    is AddDebtActivityViewModel.LoadingState.OnStart -> showProgressDialog()
                    is AddDebtActivityViewModel.LoadingState.OnStop -> dismissProgressDialog()
                    is AddDebtActivityViewModel.LoadingState.OnError -> showSnackBarUserNotExist()
                }
            }.addTo(weakCompositeDisposable)
            isConnectionAvailable.subscribe {
                onConnectionChanged(it)
            }.addTo(weakCompositeDisposable)
        }
    }

    private fun setInitialCountry() {
        text_phone.countryData = getInitialCountry()
    }

    private fun getInitialCountry(): CountryData {
        val countryCode = telephonyManager.getUserCountryCode()
        val country = countryList.find { it.code == countryCode }
        return country ?: CountryData.EMPTY
    }

    private fun onConnectionChanged(isAvailable: Boolean) {
        view_no_connection.isVisible = !isAvailable
        viewModel.command.onNext(AddDebtActivityViewModel.Events.SetOptionsMenuEnabled(isAvailable))
    }

    private fun showSnackBarError(@StringRes msgId: Int) {
        Snackbar.make(layout_coordinator, msgId, Snackbar.LENGTH_LONG).show()
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