package com.pechuro.cashdebts.ui.fragment.localdebtlist

import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.base.ItemTouchHelper
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.ItemSwipeCallback
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*

@Module
class LocalDebtListFragmentModule {

    @Provides
    @FragmentScope
    fun provideDateFormatter() = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    @Provides
    @FragmentScope
    fun provideItemSwipeToDeleteCallback() = ItemSwipeCallback()

    @Provides
    @FragmentScope
    fun provideItemSwipeToDeleteHelper(callback: ItemSwipeCallback) = ItemTouchHelper(callback)

    companion object {
        const val DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm"
    }
}
