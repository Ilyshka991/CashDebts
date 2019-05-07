package com.pechuro.cashdebts.ui.fragment.countryselection

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.countryselection.adapter.CountrySelectionAdapter
import dagger.Module
import dagger.Provides

@Module
class CountrySelectionFragmentModule {

    @Provides
    @FragmentScope
    fun provideAdapter() = CountrySelectionAdapter()

    @Provides
    @FragmentScope
    fun provideLayoutManager(fragment: CountrySelectionFragment): RecyclerView.LayoutManager =
        LinearLayoutManager(fragment.context, RecyclerView.VERTICAL, false)
}