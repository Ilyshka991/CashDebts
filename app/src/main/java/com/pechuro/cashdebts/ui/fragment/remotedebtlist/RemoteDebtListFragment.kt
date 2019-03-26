package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtListAdapter
import com.pechuro.cashdebts.ui.utils.EventBus
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

    companion object {
        fun newInstance() = RemoteDebtListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}