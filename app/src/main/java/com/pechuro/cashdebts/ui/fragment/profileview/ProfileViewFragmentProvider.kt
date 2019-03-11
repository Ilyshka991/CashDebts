package com.pechuro.cashdebts.ui.fragment.profileview

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ProfileViewFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): ProfileViewFragment
}
