package com.pechuro.cashdebts.ui.activity.adddebt.user

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDebtUserFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): AddDebtUserFragment
}
