package com.pechuro.cashdebts.ui.fragment.localdebtlist

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.LocalDebtListAdapter
import com.pechuro.cashdebts.ui.utils.EventBus
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
        setListeners()
    }

    private fun setupView() {
        recycler.apply {
            adapter = this@LocalDebtListFragment.adapter
        }
    }

    private fun setListeners() {
        fab_add.setOnClickListener {
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