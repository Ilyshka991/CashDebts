package com.pechuro.cashdebts.ui.fragment.localdebtlist

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.base.ItemTouchHelper
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.LocalDebtItemSwipeCallback
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
    fun provideLayoutManager(fragment: LocalDebtListFragment): RecyclerView.LayoutManager =
        LinearLayoutManager(fragment.context, RecyclerView.VERTICAL, false)

    @Provides
    @FragmentScope
    fun provideItemSwipeToDeleteHelper(callback: LocalDebtItemSwipeCallback) = ItemTouchHelper(callback)

    companion object {
        const val DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm"
    }
}
