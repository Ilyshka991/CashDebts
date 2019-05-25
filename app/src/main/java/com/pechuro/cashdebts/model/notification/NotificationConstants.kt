package com.pechuro.cashdebts.model.notification

object NotificationConstants {

    object DebtActionsChannelGroup {
        const val GROUP_ID = "debtActionsGroup"

        const val CHANNEL_ADD_ID = "channelAdd"
        const val CHANNEL_COMPLETE_ID = "channelComplete"
        const val CHANNEL_UPDATE_ID = "channelUpdate"
    }

    object Group {
        const val ID_GROUP = "createGroup"
    }

    enum class Action {
        ADD_ACCEPT, ADD_REJECT
    }

    object NotificationIds {
        const val NOTIFICATION_COMPLETE = 2
        const val NOTIFICATION_UPDATE = 3
    }
}