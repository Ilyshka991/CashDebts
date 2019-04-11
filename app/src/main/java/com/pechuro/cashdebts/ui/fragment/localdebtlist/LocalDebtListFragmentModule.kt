package com.pechuro.cashdebts.ui.fragment.localdebtlist

import androidx.recyclerview.widget.ItemTouchHelper
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.ItemSwipeToDeleteCallback
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
    fun provideItemSwipeToDeleteCallback(): ItemTouchHelper.SimpleCallback = ItemSwipeToDeleteCallback()


    @Provides
    @FragmentScope
    fun provideItemSwipeToDeleteHelper(callback: ItemTouchHelper.SimpleCallback) = ItemTouchHelper(callback)

    companion object {
        const val DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm"
    }
}
