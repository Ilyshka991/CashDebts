package com.pechuro.cashdebts.ui.fragment.remotedebtlist.data

import java.util.*

data class RemoteDebt(
    val id: String,
    val value: Double,
    val description: String,
    val date: Date,
    var isExpanded: Boolean = false
) {
    fun isEmpty() = value == 0.0


    companion object {
        val EMPTY = RemoteDebt("", 0.0, "", Date(), false)
    }
}