package com.pechuro.cashdebts.ui.fragment.datetimepicker

import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DateTimePickerDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): DateTimePickerDialog
}
