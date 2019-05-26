package com.pechuro.cashdebts.model.notification

data class NotificationCreateData(
    val id: String,
    val personName: String,
    val value: Double
)