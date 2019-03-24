package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtListAdapter
import dagger.Module
import dagger.Provides

@Module
class RemoteDebtListFragmentModule {

    @Provides
    @FragmentScope
    fun provideAdapter() = RemoteDebtListAdapter()
}