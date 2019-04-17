package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.debtuserprofile.DebtUserProfileDialogProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface RemoteDebtListFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            RemoteDebtListFragmentModule::class,
            DebtUserProfileDialogProvider::class]
    )
    fun bind(): RemoteDebtListFragment
}
