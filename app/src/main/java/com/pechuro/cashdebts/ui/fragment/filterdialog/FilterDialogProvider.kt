package com.pechuro.cashdebts.ui.fragment.filterdialog

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FilterDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): FilterDialog
}