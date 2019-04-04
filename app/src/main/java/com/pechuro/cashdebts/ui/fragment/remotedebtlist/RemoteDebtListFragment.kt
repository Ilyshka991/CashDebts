package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtEvent
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtListAdapter
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_remote_debt_list.*
import javax.inject.Inject

class RemoteDebtListFragment : BaseFragment<RemoteDebtListFragmentViewModel>() {
    @Inject
    lateinit var adapter: RemoteDebtListAdapter

    override val layoutId: Int
        get() = R.layout.fragment_remote_debt_list

    override fun getViewModelClass() = RemoteDebtListFragmentViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setListeners()
        setEventListeners()
    }

    private fun setupView() {
        recycler.apply {
            adapter = this@RemoteDebtListFragment.adapter
        }
    }

    private fun setListeners() {
        fab_add.setOnClickListener {
            EventBus.publish(MainActivityEvent.OpenAddActivity(false))
        }
    }

    private fun setEventListeners() {
        EventBus.listen(AddDebtEvent::class.java).subscribe {
            when (it) {
                is AddDebtEvent.OnSuccess -> showSnackbar(R.string.msg_success)
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun showSnackbar(@StringRes msgId: Int) {
        coordinator.postDelayed(
            { Snackbar.make(coordinator, msgId, Snackbar.LENGTH_SHORT).show() },
            SNACKBAR_SHOW_DELAY
        )
    }

    companion object {
        private const val SNACKBAR_SHOW_DELAY = 250L

        fun newInstance() = RemoteDebtListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}