package com.pechuro.cashdebts.ui.fragment.countryselection

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.countryselection.adapter.CountrySelectionAdapter
import com.pechuro.cashdebts.ui.utils.EventManager
import com.pechuro.cashdebts.ui.utils.binding.receiveQueryChangesFrom
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_country_selection.*
import javax.inject.Inject

class CountrySelectionFragment : BaseFragment<CountrySelectionFragmentViewModel>() {
    @Inject
    protected lateinit var adapter: CountrySelectionAdapter
    @Inject
    protected lateinit var layoutManager: RecyclerView.LayoutManager

    override val layoutId: Int
        get() = R.layout.fragment_country_selection

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
            EventManager.publish(CountrySelectionFragmentEvent.OnCountrySelect(it))
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

