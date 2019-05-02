package com.pechuro.cashdebts.ui.fragment.picturetakeoptions

import com.pechuro.cashdebts.di.annotations.ChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface PictureTakeOptionDialogProvider {

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bind(): PictureTakeOptionsDialog
}