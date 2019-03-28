package com.pechuro.cashdebts.ui.fragment.countyselection

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.countyselection.adapter.CountrySelectionAdapter
import com.pechuro.cashdebts.ui.utils.receiveQueryChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_country_select.*
import javax.inject.Inject

class CountrySelectionFragment : BaseFragment<CountrySelectionFragmentViewModel>() {
    @Inject
    protected lateinit var adapter: CountrySelectionAdapter

    override val layoutId: Int
        get() = R.layout.fragment_country_select

    override fun getViewModelClass() = CountrySelectionFragmentViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    override fun onStart() {
        super.onStart()
        subscribeToData()
    }

    private fun setupView() {
        recycler.apply {
            adapter = this@CountrySelectionFragment.adapter
        }
        viewModel.searchQuery.receiveQueryChangesFrom(search)
    }

    private fun subscribeToData() {
        viewModel.countriesListSource.subscribe {
            adapter.updateCountries(it)
            println(it.dataList.size)
        }.addTo(weakCompositeDisposable)
    }

    companion object {
        fun newInstance() = CountrySelectionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}

