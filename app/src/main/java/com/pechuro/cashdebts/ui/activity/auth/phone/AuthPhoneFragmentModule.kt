package com.pechuro.cashdebts.ui.activity.auth.phone

import android.content.Context
import com.pechuro.cashdebts.di.annotations.FragmentScope
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import dagger.Module
import dagger.Provides

@Module
class AuthPhoneFragmentModule {

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
}