package com.pechuro.cashdebts.ui.activity.adddebt.info

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.utils.receiveDateChangesFrom
import com.pechuro.cashdebts.ui.utils.receiveDecimalChangesFrom
import com.pechuro.cashdebts.ui.utils.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_debt_info.*

class AddDebtInfoFragment : BaseFragment<AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_info
    override val isViewModelShared: Boolean
        get() = true

    private val isInternetRequired by lazy {
        arguments?.getBoolean(ARG_IS_INTERNET_REQUERED) ?: false
    }

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    private fun setViewListeners() {
        with(viewModel.debt) {
            value.receiveDecimalChangesFrom(text_value)
            description.receiveTextChangesFrom(text_description)
            date.receiveDateChangesFrom(text_date)
        }
    }

    private fun setViewModelListeners() {
        with(viewModel) {
            command.subscribe {
                when (it) {
                    is AddDebtActivityViewModel.Events.ShowProgress -> showProgressDialog()
                    is AddDebtActivityViewModel.Events.DismissProgress -> dismissProgressDialog()
                }
            }.addTo(weakCompositeDisposable)
            isConnectionAvailable.subscribe {

            }
        }
    }

    companion object {
        const val TAG = "AddDebtInfoFragment"
        private const val ARG_IS_INTERNET_REQUERED = "isInternetRequired"

        fun newInstance(isInternetRequired: Boolean = false) = AddDebtInfoFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_IS_INTERNET_REQUERED, isInternetRequired)
            }
        }
    }
}