package com.pechuro.cashdebts.ui.fragment.debtuserprofiledialog

import com.pechuro.cashdebts.di.annotations.ChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DebtUserProfileDialogProvider {

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bind(): DebtUserProfileDialog
}
