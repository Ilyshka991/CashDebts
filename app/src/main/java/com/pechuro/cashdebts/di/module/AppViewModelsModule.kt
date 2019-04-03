package com.pechuro.cashdebts.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.cashdebts.di.annotations.ViewModelKey
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.countryselection.CountrySelectionActivityViewModel
import com.pechuro.cashdebts.ui.activity.main.MainActivityViewModel
import com.pechuro.cashdebts.ui.activity.profileedit.ProfileEditActivityViewModel
import com.pechuro.cashdebts.ui.fragment.countyselection.CountrySelectionFragmentViewModel
import com.pechuro.cashdebts.ui.fragment.datetimepicker.DateTimePickerDialogViewModel
import com.pechuro.cashdebts.ui.fragment.localdebtlist.LocalDebtListFragmentViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragmentViewModel
import com.pechuro.cashdebts.ui.fragment.profileview.ProfileViewFragmentViewModel
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.RemoteDebtListFragmentViewModel
import com.pechuro.cashdebts.ui.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AppViewModelsModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun mainActivity(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountrySelectionActivityViewModel::class)
    fun countrySelectActivity(viewModel: CountrySelectionActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthActivityViewModel::class)
    fun authActivity(viewModel: AuthActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountrySelectionFragmentViewModel::class)
    fun countrySelectFragment(viewModel: CountrySelectionFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddDebtActivityViewModel::class)
    fun addDebtActivity(viewModel: AddDebtActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocalDebtListFragmentViewModel::class)
    fun localDebtListFragment(viewModel: LocalDebtListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileEditFragmentViewModel::class)
    fun profileEditFragment(viewModel: ProfileEditFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewFragmentViewModel::class)
    fun profileViewFragment(viewModel: ProfileViewFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileEditActivityViewModel::class)
    fun profileEditActivity(viewModel: ProfileEditActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RemoteDebtListFragmentViewModel::class)
    fun remoteDebtListFragment(viewModel: RemoteDebtListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DateTimePickerDialogViewModel::class)
    fun dateTimePickerDialog(viewModel: DateTimePickerDialogViewModel): ViewModel
}