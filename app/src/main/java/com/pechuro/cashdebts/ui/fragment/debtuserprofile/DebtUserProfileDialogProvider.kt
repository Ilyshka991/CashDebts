package com.pechuro.cashdebts.ui.fragment.debtuserprofile

import com.pechuro.cashdebts.di.annotations.ChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DebtUserProfileDialogProvider {

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bind(): DebtUserProfileDialog
}
