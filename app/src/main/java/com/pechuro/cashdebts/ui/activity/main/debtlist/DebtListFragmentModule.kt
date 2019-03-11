package com.pechuro.cashdebts.ui.activity.main.debtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.activity.main.debtlist.adapter.DebtListAdapter
import dagger.Module
import dagger.Provides

@Module
class DebtListFragmentModule {

    @Provides
    @FragmentScope
    fun provideAdapter() = DebtListAdapter()
}