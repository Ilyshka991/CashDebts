package com.pechuro.cashdebts.ui.activity.auth.countyselect

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentCountrySelectBinding
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.auth.Events
import com.pechuro.cashdebts.ui.activity.auth.countyselect.adapter.CountrySelectAdapter
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.ui.utils.EventBus
import javax.inject.Inject

class CountrySelectFragment : BaseFragment<FragmentCountrySelectBinding, AuthActivityViewModel>() {
    @Inject
    protected lateinit var adapter: CountrySelectAdapter

    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AuthActivityViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = R.layout.fragment_country_select

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        EventBus.listen(CountySelectEvent::class.java).subscribe {
            when (it) {
                is CountySelectEvent.OnCountySelect -> onCountrySelect(it.country)
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun setupView() {
        with(viewDataBinding) {
            recycler.apply {
                adapter = this@CountrySelectFragment.adapter
            }

            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(query: String?): Boolean {
                    query?.let { adapter.filterCountries(it) }
                    return true
                }
            })
        }
    }

    private fun onCountrySelect(country: CountryData) {
        viewModel.countryData.set(country)
        viewModel.command.onNext(Events.CloseCountrySelection)
    }

    companion object {
        fun newInstance() = CountrySelectFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}

sealed class CountySelectEvent : BaseEvent() {
    class OnCountySelect(val country: CountryData) : CountySelectEvent()
}