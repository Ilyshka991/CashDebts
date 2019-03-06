package com.pechuro.cashdebts.ui.activity.auth.countyselect

import android.content.Context
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import dagger.Module
import dagger.Provides
import java.io.BufferedInputStream

@Module
class CountrySelectModule {

    @Provides
    @FragmentScope
    fun provideAdapter(data: List<CountryData>) = CountrySelectAdapter(data)

    @Provides
    @FragmentScope
    fun provideCountryList(context: Context): List<CountryData> {
        val reader = BufferedInputStream(context.resources.assets.open("countries.txt"))
        val resultList = mutableListOf<CountryData>()
        resultList += CountryData("By", "375", "Belarus", "__ ___ __ __")
        return resultList
    }
}