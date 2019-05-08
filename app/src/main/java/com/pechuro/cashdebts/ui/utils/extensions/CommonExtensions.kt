package com.pechuro.cashdebts.ui.utils.extensions

import com.pechuro.cashdebts.model.entity.CountryData

fun String.getFormattedNumber(countryList: List<CountryData>): String {
    for (i in this.lastIndex downTo 1) {
        val possiblePrefix = slice(0..i)
        val possibleData = countryList.findLast { possiblePrefix == it.phonePrefix }
        if (possibleData != null && !possibleData.phonePattern.isNullOrEmpty()) {
            val builder = StringBuilder()
            builder.append(possibleData.phonePrefix).append(' ')
            var index = possibleData.phonePrefix.length
            possibleData.phonePattern.forEach { char ->
                if (char != ' ') {
                    builder.append(this[index])
                    index++
                } else {
                    builder.append(' ')
                }
            }
            return builder.toString()
        }
    }
    return this
}