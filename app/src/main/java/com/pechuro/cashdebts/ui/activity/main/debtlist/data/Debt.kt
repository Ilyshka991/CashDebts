package com.pechuro.cashdebts.ui.activity.main.debtlist.data

data class Debt(
    val id: String,
    val person: String,
    val value: Double,
    val description: String?
)