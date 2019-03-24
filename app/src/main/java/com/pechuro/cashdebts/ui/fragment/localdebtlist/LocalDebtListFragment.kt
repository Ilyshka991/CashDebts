package com.pechuro.cashdebts.ui.fragment.localdebtlist

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentLocalDebtListBinding
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.LocalDebtListAdapter
import com.pechuro.cashdebts.ui.utils.EventBus
import javax.inject.Inject

class LocalDebtListFragment : BaseFragment<FragmentLocalDebtListBinding, LocalDebtListFragmentViewModel>() {
    @Inject
    lateinit var adapter: LocalDebtListAdapter

    override val layoutId: Int
        get() = R.layout.fragment_local_debt_list

    override fun getViewModelClass() = LocalDebtListFragmentViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setListeners()
    }

    private fun setupView() {
        viewDataBinding.recyclerView.apply {
            adapter = this@LocalDebtListFragment.adapter
        }
    }

    private fun setListeners() {
        viewDataBinding.fabAdd.setOnClickListener {
            EventBus.publish(MainActivityEvent.OpenAddActivity(true))
        }
    }

    companion object {
        fun newInstance() = LocalDebtListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}