package com.pechuro.cashdebts.ui.activity.adddebt.remoteuser

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDebtRemoteUserFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): AddDebtRemoteUserFragment
}
