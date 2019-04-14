package com.pechuro.cashdebts.ui.fragment.countyselection

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.countryselection.CountySelectEvent
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.countyselection.adapter.CountrySelectionAdapter
import com.pechuro.cashdebts.ui.utils.EventBus
import com.pechuro.cashdebts.ui.utils.binding.receiveQueryChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_country_select.*
import javax.inject.Inject

class CountrySelectionFragment : BaseFragment<CountrySelectionFragmentViewModel>() {
    @Inject
    protected lateinit var adapter: CountrySelectionAdapter
    @Inject
    protected lateinit var layoutManager: RecyclerView.LayoutManager

    override val layoutId: Int
        get() = R.layout.fragment_country_select

    override fun getViewModelClass() = CountrySelectionFragmentViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onStart() {
        super.onStart()
        subscribeToData()
    }

    private fun setupView() {
        recycler.apply {
            layoutManager = this@CountrySelectionFragment.layoutManager
            adapter = this@CountrySelectionFragment.adapter
        }
        adapter.clickEmitter.subscribe {
            EventBus.publish(CountySelectEvent.OnCountySelect(it))
        }.addTo(strongCompositeDisposable)
        viewModel.searchQuery.receiveQueryChangesFrom(search).addTo(strongCompositeDisposable)
    }

    private fun subscribeToData() {
        viewModel.countriesListSource.subscribe(adapter::updateCountries).addTo(weakCompositeDisposable)
    }

    companion object {
        fun newInstance() = CountrySelectionFragment()
    }
}

