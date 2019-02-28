package com.pechuro.cashdebts.di.module

import com.pechuro.cashdebts.di.annotations.ActivityScope
import com.pechuro.cashdebts.ui.activity.add.AddActivity
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.auth.code.AuthCodeFragmentProvider
import com.pechuro.cashdebts.ui.activity.auth.phone.AuthPhoneFragmentProvider
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import com.pechuro.cashdebts.ui.fragment.adddebt.AddDebtFragmentProvider
import com.pechuro.cashdebts.ui.fragment.debtlist.DebtListFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [DebtListFragmentProvider::class])
    fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AddDebtFragmentProvider::class])
    fun bindAddActivity(): AddActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            AuthPhoneFragmentProvider::class,
            AuthCodeFragmentProvider::class]
    )
    fun bindAuthActivity(): AuthActivity
}