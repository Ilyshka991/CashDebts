package com.pechuro.cashdebts.ui.activity.auth.code

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AuthCodeFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): AuthCodeFragment
}
