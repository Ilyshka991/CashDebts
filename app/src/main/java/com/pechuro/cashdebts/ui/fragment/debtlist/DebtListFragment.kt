package com.pechuro.cashdebts.ui.fragment.debtlist

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentDebtListBinding
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.debtlist.adapter.DebtListAdapter
import javax.inject.Inject

class DebtListFragment : BaseFragment<FragmentDebtListBinding, DebtListViewModel>() {
    @Inject
    lateinit var adapter: DebtListAdapter
    @Inject
    lateinit var layoutManager: LinearLayoutManager

    override val viewModel: DebtListViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(DebtListViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_debt_list

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        viewDataBinding.recyclerView.apply {
            layoutManager = this@DebtListFragment.layoutManager
            adapter = this@DebtListFragment.adapter
        }
    }

    companion object {
        fun newInstance() = DebtListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}