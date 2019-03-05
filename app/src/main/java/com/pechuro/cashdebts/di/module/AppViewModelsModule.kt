package com.pechuro.cashdebts.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.cashdebts.di.annotations.ViewModelKey
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtActivityViewModel
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.activity.main.MainActivityViewModel
import com.pechuro.cashdebts.ui.activity.main.debtlist.DebtListFragmentViewModel
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
    @ViewModelKey(AuthActivityViewModel::class)
    fun authActivity(viewModel: AuthActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddDebtActivityViewModel::class)
    fun addDebtActivity(viewModel: AddDebtActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DebtListFragmentViewModel::class)
    fun debtListFragment(viewModel: DebtListFragmentViewModel): ViewModel
}