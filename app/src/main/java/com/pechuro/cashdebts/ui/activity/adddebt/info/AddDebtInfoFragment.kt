package com.pechuro.cashdebts.ui.activity.adddebt.info

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.calculator.Result
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.datetimepicker.DateTimePickerDialog
import com.pechuro.cashdebts.ui.fragment.datetimepicker.DateTimePickerEvent
import com.pechuro.cashdebts.ui.utils.EventManager
import com.pechuro.cashdebts.ui.utils.binding.receiveDateChangesFrom
import com.pechuro.cashdebts.ui.utils.binding.receiveTextChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_debt_info.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AddDebtInfoFragment : BaseFragment<AddDebtActivityViewModel>() {

    @Inject
    protected lateinit var imm: InputMethodManager
    @Inject
    protected lateinit var dateFormatter: SimpleDateFormat

    override val layoutId: Int
        get() = R.layout.fragment_add_debt_info
    override val isViewModelShared: Boolean
        get() = true

    private val isInternetRequired by lazy {
        arguments?.getBoolean(ARG_IS_INTERNET_REQUIRED) ?: false
    }

    override fun getViewModelClass() = AddDebtActivityViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
        setupView()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
        setEventListeners()
    }

    private fun setEventListeners() {
        EventManager.listen(DateTimePickerEvent::class.java).subscribe {
            when (it) {
                is DateTimePickerEvent.OnDateSelected -> onDateSelected(it.date)
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun setViewListeners() {
        with(viewModel) {
            strongCompositeDisposable.addAll(
                mathExpression.receiveTextChangesFrom(text_value),
                debt.description.receiveTextChangesFrom(text_description),
                debt.date.receiveDateChangesFrom(text_date, dateFormatter)
            )
        }
        text_date.setOnClickListener {
            showDateTimePicker()
        }
        text_value.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                text_description.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }
        text_description.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.save()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setupView() {
        if (!imm.isActive) imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    private fun setViewModelListeners() {
        with(viewModel) {
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
                        is Result.Success -> getString(
                            R.string.fragment_add_debt_info_text_sum,
                            result.result
                        )
                        is Result.Error -> getString(R.string.fragment_add_debt_info_error_sum)
                    }
                }
            }.addTo(weakCompositeDisposable)
            loadingState.subscribe {
                when (it) {
                    is AddDebtActivityViewModel.LoadingState.OnStart -> showProgressDialog()
                    is AddDebtActivityViewModel.LoadingState.OnStop -> dismissProgressDialog()
                }
            }.addTo(weakCompositeDisposable)
        }
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