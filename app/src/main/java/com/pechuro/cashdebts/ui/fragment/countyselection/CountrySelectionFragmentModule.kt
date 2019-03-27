package com.pechuro.cashdebts.ui.fragment.countyselection

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.countyselection.adapter.CountrySelectionAdapter
import dagger.Module
import dagger.Provides

@Module
class CountrySelectionFragmentModule {

    @Provides
    @FragmentScope
    fun provideAdapter() = CountrySelectionAdapter()
}