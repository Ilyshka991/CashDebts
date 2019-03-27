package com.pechuro.cashdebts.di.module

import android.content.Context
import com.pechuro.cashdebts.model.entity.CountryData
import dagger.Module
import dagger.Provides

@Module
class CountryListModule {

    @Provides
    fun provideCountryList(context: Context): List<CountryData> {
        val resultList = mutableListOf<CountryData>()
        val reader = context.resources.assets.open("countries.txt").bufferedReader()
        reader.forEachLine { line ->
            resultList += line.split(';').let {
                CountryData(
                    it[0],
                    "+${it[1]}",
                    it[2],
                    if (it.size == 4) it[3] else null
                )
            }
        }
        resultList.sortBy { it.name }
        return resultList
    }
}