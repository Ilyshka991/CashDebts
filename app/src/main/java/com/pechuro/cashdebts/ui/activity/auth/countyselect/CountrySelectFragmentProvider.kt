package com.pechuro.cashdebts.ui.activity.auth.countyselect

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface CountrySelectFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            CountrySelectModule::class]
    )
    fun bind(): CountrySelectFragment
}
