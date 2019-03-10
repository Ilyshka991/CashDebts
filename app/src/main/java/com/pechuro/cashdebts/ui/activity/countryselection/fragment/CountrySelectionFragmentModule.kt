package com.pechuro.cashdebts.ui.activity.countryselection.fragment

import android.content.Context
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.activity.countryselection.fragment.adapter.CountrySelectionAdapter
import com.pechuro.cashdebts.ui.activity.countryselection.fragment.adapter.CountrySelectionDiffCallback
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import dagger.Module
import dagger.Provides

@Module
class CountrySelectionFragmentModule {

    @Provides
    @FragmentScope
    fun provideCountryList(context: Context): List<CountryData> {
        val resultList = mutableListOf<CountryData>()
        val reader = context.resources.assets.open("countries.txt").bufferedReader()
        reader.forEachLine { line ->
            resultList += line.split(';').let {
                CountryData(it[0], "+${it[1]}", it[2], if (it.size == 4) it[3] else null)
            }
        }
        resultList.sortBy { it.name }
        return resultList
    }

    @Provides
    @FragmentScope
    fun provideAdapter(diffCallback: CountrySelectionDiffCallback, countries: List<CountryData>) =
        CountrySelectionAdapter(
            diffCallback,
            countries
        )

    @Provides
    @FragmentScope
    fun provideDiffCallback() =
        CountrySelectionDiffCallback()
}