package com.pechuro.cashdebts.ui.activity.adddebt.localuser

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDebtLocalUserFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): AddDebtLocalUserFragment
}
