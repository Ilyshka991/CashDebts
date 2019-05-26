package com.pechuro.cashdebts.model.notification

object NotificationStructure {

    object CommonStructure {
        const val TYPE = "type"
    }

    object CreateStructure {
        const val ID = "id"
        const val PERSON_NAME = "personName"
        const val VALUE = "value"
    }

    object Types {
        const val CREATE = "create"
        const val UPDATE = "update"
    }
}