package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface RemoteDebtListFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [RemoteDebtListFragmentModule::class])
    fun bind(): RemoteDebtListFragment
}
