package com.pechuro.cashdebts.ui.fragment.localdebtlist.data

import java.util.*

data class LocalDebt(
    val personName: String,
    val value: Double,
    val description: String?,
    val date: Date
)