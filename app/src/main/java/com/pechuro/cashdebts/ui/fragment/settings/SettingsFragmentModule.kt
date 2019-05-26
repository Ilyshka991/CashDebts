package com.pechuro.cashdebts.ui.fragment.settings

import android.content.Context
import android.widget.SpinnerAdapter
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.fragment.settings.adapter.SettingSpinnerAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SettingsFragmentModule {

    @Provides
    @FragmentScope
    @Named(LANGUAGES)
    fun provideLanguagesAdapter(context: Context): SpinnerAdapter {
        val languages = context.resources.getStringArray(R.array.languages)
        return SettingSpinnerAdapter(languages)
    }

    @Provides
    @FragmentScope
    @Named(LANGUAGES)
    fun provideLanguageIds(context: Context): Array<String> =
        context.resources.getStringArray(R.array.languages_id)

    @Provides
    @FragmentScope
    @Named(THEMES)
    fun provideThemesAdapter(context: Context): SpinnerAdapter {
        val languages = context.resources.getStringArray(R.array.themes)
        return SettingSpinnerAdapter(languages)
    }

    @Provides
    @FragmentScope
    @Named(THEMES)
    fun provideThemesIds(context: Context): Array<String> =
        context.resources.getStringArray(R.array.themes_id)

    companion object {
        const val LANGUAGES = "languages"
        const val THEMES = "themes"
    }
}