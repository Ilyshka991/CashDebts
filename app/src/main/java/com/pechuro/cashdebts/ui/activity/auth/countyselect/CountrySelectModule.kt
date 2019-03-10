package com.pechuro.cashdebts.ui.activity.auth.countyselect

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.activity.auth.countyselect.adapter.CountrySelectAdapter
import com.pechuro.cashdebts.ui.activity.auth.countyselect.adapter.CountrySelectDiffCallback
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import dagger.Module
import dagger.Provides

@Module
class CountrySelectModule {

    @Provides
    @FragmentScope
    fun provideAdapter(diffCallback: CountrySelectDiffCallback, countries: List<CountryData>) =
        CountrySelectAdapter(diffCallback, countries)

    @Provides
    @FragmentScope
    fun provideDiffCallback() = CountrySelectDiffCallback()
}