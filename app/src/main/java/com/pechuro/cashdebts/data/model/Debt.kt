package com.pechuro.cashdebts.data.model

data class Debt(
    val id: String,
    val person: String,
    val value: Double,
    val description: String?
)