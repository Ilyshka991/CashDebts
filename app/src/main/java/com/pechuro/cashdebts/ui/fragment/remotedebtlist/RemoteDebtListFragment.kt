package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentRemoteDebtListBinding
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtListAdapter
import com.pechuro.cashdebts.ui.utils.EventBus
import javax.inject.Inject

class RemoteDebtListFragment : BaseFragment<FragmentRemoteDebtListBinding, RemoteDebtListFragmentViewModel>() {
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
        viewDataBinding.recyclerView.apply {
            adapter = this@RemoteDebtListFragment.adapter
        }
    }

    private fun setListeners() {
        viewDataBinding.fabAdd.setOnClickListener {
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