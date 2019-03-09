package com.pechuro.cashdebts.ui.activity.auth.countyselect

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import dagger.Module
import dagger.Provides

@Module
class CountrySelectModule {

    @Provides
    @FragmentScope
    fun provideAdapter(data: List<CountryData>) = CountrySelectAdapter(data)
}