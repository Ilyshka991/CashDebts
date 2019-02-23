package com.pechuro.cashdebts.ui.fragment.debtlist

import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.debtlist.adapter.DebtListAdapter
import dagger.Module
import dagger.Provides

@Module
class DebtListFragmentModule {

    @Provides
    fun provideLayoutManager(fragment: DebtListFragment) = LinearLayoutManager(fragment.context)

    @Provides
    @FragmentScope
    fun provideAdapter() = DebtListAdapter()
}