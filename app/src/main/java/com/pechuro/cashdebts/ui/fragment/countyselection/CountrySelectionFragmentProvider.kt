package com.pechuro.cashdebts.ui.fragment.countyselection

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface CountrySelectionFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [CountrySelectionFragmentModule::class]
    )
    fun bind(): CountrySelectionFragment
}
