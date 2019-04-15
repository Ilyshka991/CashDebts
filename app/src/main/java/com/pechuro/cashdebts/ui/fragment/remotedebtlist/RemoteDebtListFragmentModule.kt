package com.pechuro.cashdebts.ui.fragment.remotedebtlist

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.base.ItemTouchHelper
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtItemSwipeCallback
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*

@Module
class RemoteDebtListFragmentModule {

    @Provides
    @FragmentScope
    fun provideDateFormatter() = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    @Provides
    @FragmentScope
    fun provideLayoutManager(fragment: RemoteDebtListFragment): RecyclerView.LayoutManager =
        LinearLayoutManager(fragment.context, RecyclerView.VERTICAL, false)

    @Provides
    @FragmentScope
    fun provideItemSwipeHelper(callback: RemoteDebtItemSwipeCallback) = ItemTouchHelper(callback)

    companion object {
        const val DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm"
    }
}