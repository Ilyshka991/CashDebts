package com.pechuro.cashdebts.ui.activity.auth.phone

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface AuthPhoneFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AuthPhoneFragmentModule::class])
    fun bind(): AuthPhoneFragment
}