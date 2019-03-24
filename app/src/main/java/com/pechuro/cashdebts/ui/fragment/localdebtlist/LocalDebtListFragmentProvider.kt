package com.pechuro.cashdebts.ui.fragment.localdebtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface LocalDebtListFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [LocalDebtListFragmentModule::class])
    fun bind(): LocalDebtListFragment
}
