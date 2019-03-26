package com.pechuro.cashdebts.ui.fragment.countyselection

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.countyselection.adapter.CountrySelectionAdapter
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

    private fun setupView() {
        recycler.apply {
            adapter = this@CountrySelectionFragment.adapter
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let { adapter.filterCountries(it) }
                return true
            }
        })
    }

    companion object {
        fun newInstance() = CountrySelectionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}

