package com.pechuro.cashdebts.ui.fragment.remotedebtlist.data

data class RemoteDebt(
    val id: String,
    val person: String,
    val value: Double,
    val description: String?
)