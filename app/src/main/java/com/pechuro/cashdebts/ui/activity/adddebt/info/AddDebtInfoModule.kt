package com.pechuro.cashdebts.ui.activity.adddebt.info

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.pechuro.cashdebts.di.annotations.FragmentScope
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*

@Module
class AddDebtInfoModule {

    @Provides
    @FragmentScope
    fun provideDateFormatter() = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    @Provides
    @FragmentScope
    fun provideImm(fragment: AddDebtInfoFragment) =
        fragment.requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    companion object {
        const val DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm"
    }
}