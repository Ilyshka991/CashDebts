package com.pechuro.cashdebts.ui.activity.main.debtlist

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentDebtListBinding
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.activity.main.debtlist.adapter.DebtListAdapter
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.utils.EventBus
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
    }

    override fun onResume() {
        super.onResume()
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
            EventBus.publish(MainActivityEvent.OpenAddActivity)
        }
    }

    private fun subscribeToData() {
        viewModel.dataSource.subscribe({ (type, value) ->
            when (type) {
                DocumentChange.Type.ADDED -> adapter.addData(value)
                DocumentChange.Type.REMOVED -> adapter.removeData(value)
                DocumentChange.Type.MODIFIED -> adapter.modifyData(value)
            }
        }, {
            it.printStackTrace()
        }).let(weakCompositeDisposable::add)
    }

    companion object {
        fun newInstance() = DebtListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}