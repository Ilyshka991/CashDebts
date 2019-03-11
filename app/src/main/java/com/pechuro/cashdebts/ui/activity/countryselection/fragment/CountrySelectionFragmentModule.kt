package com.pechuro.cashdebts.ui.activity.countryselection.fragment

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.activity.countryselection.fragment.adapter.CountrySelectionAdapter
import com.pechuro.cashdebts.ui.activity.countryselection.fragment.adapter.CountrySelectionDiffCallback
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import dagger.Module
import dagger.Provides

@Module
class CountrySelectionFragmentModule {

    @Provides
    @FragmentScope
    fun provideAdapter(diffCallback: CountrySelectionDiffCallback, countries: List<CountryData>) =
        CountrySelectionAdapter(
            diffCallback,
            countries
        )

    @Provides
    @FragmentScope
    fun provideDiffCallback() =
        CountrySelectionDiffCallback()
}