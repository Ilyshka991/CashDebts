package com.pechuro.cashdebts.ui.fragment.localdebtlist.data

data class LocalDebt(
    val id: String,
    val person: String,
    val value: Double,
    val description: String?
)