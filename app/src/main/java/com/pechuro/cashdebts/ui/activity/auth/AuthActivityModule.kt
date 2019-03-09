package com.pechuro.cashdebts.ui.activity.auth

import android.content.Context
import com.pechuro.cashdebts.di.annotations.ActivityScope
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import dagger.Module
import dagger.Provides

@Module
class AuthActivityModule {

    @Provides
    @ActivityScope
    fun provideCountryList(context: Context): List<CountryData> {
        val resultList = mutableListOf<CountryData>()
        val reader = context.resources.assets.open("countries.txt").bufferedReader()
        reader.forEachLine { line ->
            resultList += line.split(';').let {
                CountryData(it[0], "+${it[1]}", it[2], if (it.size == 4) it[3] else null)
            }
        }
        return resultList
    }
}