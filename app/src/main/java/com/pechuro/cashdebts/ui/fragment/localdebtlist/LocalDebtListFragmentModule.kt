package com.pechuro.cashdebts.ui.fragment.localdebtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.LocalDebtListAdapter
import dagger.Module
import dagger.Provides

@Module
class LocalDebtListFragmentModule {

    @Provides
    @FragmentScope
    fun provideAdapter() = LocalDebtListAdapter()
}