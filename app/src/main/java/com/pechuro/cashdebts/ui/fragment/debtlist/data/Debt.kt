package com.pechuro.cashdebts.ui.fragment.debtlist.data

data class Debt(
    val id: String,
    val person: String,
    val value: Double,
    val description: String?
)