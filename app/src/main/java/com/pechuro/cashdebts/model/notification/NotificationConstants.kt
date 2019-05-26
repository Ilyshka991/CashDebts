package com.pechuro.cashdebts.model.notification

object NotificationConstants {

    object DebtActionsChannelGroup {
        const val GROUP_ID = "debtActionsGroup"

        const val CHANNEL_ADD_ID = "channelAdd"
        const val CHANNEL_UPDATE_ID = "channelUpdate"
    }

    object Group {
        const val ID_CREATE_GROUP = "createGroup"
    }

    object Action {
        const val ADD_ACCEPT = 1
        const val ADD_REJECT = 2
    }

    object NotificationIds {
        const val NOTIFICATION_UPDATE = 3
    }
}