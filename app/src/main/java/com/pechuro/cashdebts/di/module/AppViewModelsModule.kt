package com.pechuro.cashdebts.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pechuro.cashdebts.di.annotations.ViewModelKey
import com.pechuro.cashdebts.ui.activity.main.MainActivityViewModel
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
    fun navigationActivity(viewModel: MainActivityViewModel): ViewModel

}