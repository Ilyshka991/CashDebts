package com.pechuro.cashdebts.model.notification

object NotificationStructure {

    object CreateStructure {
        const val ID = "id"
        const val TYPE = "type"
        const val PERSON_NAME = "personName"
        const val VALUE = "value"
    }

    object Types {
        const val CREATE = "create"
        const val UPDATE = "update"
    }
}