package com.pechuro.cashdebts.ui.activity.adddebt.info

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDebtInfoFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AddDebtInfoModule::class])
    fun bind(): AddDebtInfoFragment
}
