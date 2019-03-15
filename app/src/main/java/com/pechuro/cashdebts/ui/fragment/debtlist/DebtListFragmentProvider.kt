package com.pechuro.cashdebts.ui.fragment.debtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DebtListFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [DebtListFragmentModule::class])
    fun bind(): DebtListFragment
}
