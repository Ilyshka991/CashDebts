package com.pechuro.cashdebts.ui.fragment.localdebtlist

import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtEvent
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.LocalDebtListAdapter
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_local_debt_list.*
import javax.inject.Inject

class LocalDebtListFragment : BaseFragment<LocalDebtListFragmentViewModel>() {
    @Inject
    lateinit var adapter: LocalDebtListAdapter

    override val layoutId: Int
        get() = R.layout.fragment_local_debt_list

    override fun getViewModelClass() = LocalDebtListFragmentViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setViewListeners()
        setEventListeners()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    private fun setupView() {
        recycler.apply {
            adapter = this@LocalDebtListFragment.adapter
        }
    }

    private fun setViewListeners() {
        fab_add.setOnClickListener {
            EventBus.publish(MainActivityEvent.OpenAddActivity(true))
        }
    }

    private fun setEventListeners() {
        EventBus.listen(AddDebtEvent::class.java).subscribe {
            when (it) {
                is AddDebtEvent.OnSuccess -> showSnackbar(R.string.msg_success)
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun setViewModelListeners() {
        viewModel.debtSource.subscribe {
            println(it)
        }.addTo(weakCompositeDisposable)
    }

    private fun showSnackbar(@StringRes msgId: Int) {
        coordinator.postDelayed(
            { Snackbar.make(coordinator, msgId, Snackbar.LENGTH_SHORT).show() },
            SNACKBAR_SHOW_DELAY
        )
    }

    companion object {
        private const val SNACKBAR_SHOW_DELAY = 250L

        fun newInstance() = LocalDebtListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}