package com.pechuro.cashdebts.ui.fragment.version

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface NewVersionFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): NewVersionFragment
}
