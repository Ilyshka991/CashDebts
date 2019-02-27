package com.pechuro.cashdebts.ui.fragment.debtlist

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentDebtListBinding
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.debtlist.adapter.DebtListAdapter
import javax.inject.Inject

class DebtListFragment : BaseFragment<FragmentDebtListBinding, DebtListFragmentViewModel>() {
    @Inject
    lateinit var adapter: DebtListAdapter
    @Inject
    lateinit var layoutManager: LinearLayoutManager

    override val viewModel: DebtListFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(DebtListFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_debt_list

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setListeners()
        subscribeToData()
    }

    private fun setupView() {
        viewDataBinding.recyclerView.apply {
            layoutManager = this@DebtListFragment.layoutManager
            adapter = this@DebtListFragment.adapter
        }
    }

    private fun setListeners() {
        viewDataBinding.fabAdd.setOnClickListener {
            viewModel.add()
        }
    }

    private fun subscribeToData() {
        viewModel.dataList.subscribe {
            adapter.setData(it)
        }.let(weakCompositeDisposable::add)
    }

    companion object {
        fun newInstance() = DebtListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}