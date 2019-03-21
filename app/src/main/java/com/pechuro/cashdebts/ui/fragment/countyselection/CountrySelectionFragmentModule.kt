package com.pechuro.cashdebts.ui.fragment.countyselection

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.fragment.countyselection.adapter.CountrySelectionAdapter
import com.pechuro.cashdebts.ui.fragment.countyselection.adapter.CountrySelectionDiffCallback
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