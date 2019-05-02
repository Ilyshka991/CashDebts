package com.pechuro.cashdebts.ui.fragment.navigationdialog

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface NavigationDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): NavigationDialog
}