package com.pechuro.cashdebts.di.module

import com.pechuro.cashdebts.di.annotations.ActivityScope
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivity
import com.pechuro.cashdebts.ui.activity.adddebt.info.AddDebtInfoFragmentProvider
import com.pechuro.cashdebts.ui.activity.adddebt.localuser.AddDebtLocalUserFragmentProvider
import com.pechuro.cashdebts.ui.activity.adddebt.remoteuser.AddDebtRemoteUserFragmentProvider
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityModule
import com.pechuro.cashdebts.ui.activity.auth.code.AuthCodeFragmentProvider
import com.pechuro.cashdebts.ui.activity.auth.phone.AuthPhoneFragmentProvider
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivity
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import com.pechuro.cashdebts.ui.activity.profileedit.ProfileEditActivity
import com.pechuro.cashdebts.ui.activity.splash.SplashActivity
import com.pechuro.cashdebts.ui.fragment.countyselection.CountrySelectionFragmentProvider
import com.pechuro.cashdebts.ui.fragment.debtlist.DebtListFragmentProvider
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragmentProvider
import com.pechuro.cashdebts.ui.fragment.profileview.ProfileViewFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector
    fun bindSplash(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            ProfileEditFragmentProvider::class,
            DebtListFragmentProvider::class,
            ProfileViewFragmentProvider::class
        ]
    )
    fun bindMain(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            AddDebtInfoFragmentProvider::class,
            AddDebtRemoteUserFragmentProvider::class,
            AddDebtLocalUserFragmentProvider::class]
    )
    fun bindAdd(): AddDebtActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ProfileEditFragmentProvider::class])
    fun bindProfileEdit(): ProfileEditActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            AuthPhoneFragmentProvider::class,
            AuthCodeFragmentProvider::class,
            ProfileEditFragmentProvider::class,
            AuthActivityModule::class]
    )
    fun bindAuth(): AuthActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [CountrySelectionFragmentProvider::class,
            AuthActivityModule::class]
    )
    fun bindCountrySelect(): CountrySelectionActivity
}