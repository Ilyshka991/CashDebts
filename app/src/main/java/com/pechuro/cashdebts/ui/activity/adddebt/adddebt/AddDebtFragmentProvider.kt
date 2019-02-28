package com.pechuro.cashdebts.ui.activity.adddebt.adddebt

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDebtFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): AddDebtFragment
}
