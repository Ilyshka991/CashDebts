package com.pechuro.cashdebts.ui.fragment.profileedit

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.picturetakeoptions.PictureTakeOptionDialogProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ProfileEditFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [PictureTakeOptionDialogProvider::class])
    fun bind(): ProfileEditFragment
}
