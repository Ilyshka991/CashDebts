package com.pechuro.cashdebts.di.module

import com.pechuro.cashdebts.di.annotations.ActivityScope
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.auth.code.AuthCodeFragmentProvider
import com.pechuro.cashdebts.ui.activity.auth.phone.AuthPhoneFragmentProvider
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    fun bindNavigationActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AuthPhoneFragmentProvider::class, AuthCodeFragmentProvider::class])
    fun bindAuthActivity(): AuthActivity
}