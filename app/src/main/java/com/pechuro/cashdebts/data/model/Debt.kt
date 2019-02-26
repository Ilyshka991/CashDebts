package com.pechuro.cashdebts.data.model

data class Debt(
    val id: String,
    val debtor: String,
    val debtee: String,
    val value: Double,
    val description: String?
)