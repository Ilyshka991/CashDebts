package com.pechuro.cashdebts.ui.activity.adddebt.info

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.calculator.Result
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.datetimepicker.DateTimePickerDialog
import com.pechuro.cashdebts.ui.fragment.datetimepicker.DateTimePickerEvent
import com.pechuro.cashdebts.ui.utils.EventBus
import com.pechuro.cashdebts.ui.utils.binding.receiveDateChangesFrom
import com.pechuro.cashdebts.ui.utils.binding.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_debt_info.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AddDebtInfoFragment : BaseFragment<AddDebtActivityViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_debt_info
    override val isViewModelShared: Boolean
        get() = true

    private val isInternetRequired by lazy {
        arguments?.getBoolean(ARG_IS_INTERNET_REQUIRED) ?: false
    }

    @Inject
    protected lateinit var dateFormatter: SimpleDateFormat

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
        setupView()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
        setEventListeners()
    }

    private fun setEventListeners() {
        EventBus.listen(DateTimePickerEvent::class.java).subscribe {
            when (it) {
                is DateTimePickerEvent.OnDateSelected -> onDateSelected(it.date)
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun setViewListeners() {
        with(viewModel) {
            mathExpression.receiveTextChangesFrom(text_value).addTo(strongCompositeDisposable)
            debt.description.receiveTextChangesFrom(text_description).addTo(strongCompositeDisposable)
            debt.date.receiveDateChangesFrom(text_date, dateFormatter).addTo(strongCompositeDisposable)
        }
        text_date.setOnClickListener {
            showDateTimePicker()
        }

        text_description.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.save()
                return@setOnEditorActionListener true
            }
            false
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
            if (isInternetRequired) {
                isConnectionAvailable.subscribe {
                    onConnectionChanged(it)
                }.addTo(weakCompositeDisposable)
            }
            debtValue.subscribe {
                text_sum.text = if (it.first) {
                    ""
                } else {
                    when (val result = it.second) {
                        is Result.Success -> getString(R.string.add_debt_msg_sum, result.result)
                        is Result.Error -> getString(R.string.add_debt_msg_sum_error)
                    }
                }
            }.addTo(weakCompositeDisposable)
        }
    }

    private fun setupView() {
        onDateSelected(Date())
    }

    private fun onDateSelected(date: Date) {
        val formattedDate = dateFormatter.format(date)
        text_date.setText(formattedDate)
    }

    private fun onConnectionChanged(isAvailable: Boolean) {
        view_no_connection.isVisible = !isAvailable
        viewModel.command.onNext(AddDebtActivityViewModel.Events.SetOptionsMenuEnabled(isAvailable))
    }

    private fun showDateTimePicker() {
        val dialog = DateTimePickerDialog.newInstance()
        dialog.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    companion object {
        private const val ARG_IS_INTERNET_REQUIRED = "isInternetRequired"

        fun newInstance(isInternetRequired: Boolean = false) = AddDebtInfoFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_IS_INTERNET_REQUIRED, isInternetRequired)
            }
        }
    }
}