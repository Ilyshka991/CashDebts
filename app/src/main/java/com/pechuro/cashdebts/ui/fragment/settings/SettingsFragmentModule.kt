package com.pechuro.cashdebts.ui.fragment.settings

import android.content.Context
import android.widget.SpinnerAdapter
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.settings.adapter.SettingLanguageAdapter
import dagger.Module
import dagger.Provides

@Module
class SettingsFragmentModule {

    @Provides
    @FragmentScope
    fun provideLanguagesAdapter(context: Context): SpinnerAdapter {
        val languages = context.resources.getStringArray(R.array.languages)
        return SettingLanguageAdapter(languages)
    }

    @Provides
    @FragmentScope
    fun provideLanguageIds(context: Context) =
        context.resources.getStringArray(R.array.languages_id)
}