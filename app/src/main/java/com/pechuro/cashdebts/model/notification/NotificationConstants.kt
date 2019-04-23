package com.pechuro.cashdebts.model.notification

object NotificationConstants {

    object DebtActionsChannelGroup {
        const val GROUP_ID = "debtActionsGroup"

        const val CHANNEL_ADD_ID = "channelAdd"
        const val CHANNEL_COMPLETE_ID = "channelComplete"
        const val CHANNEL_UPDATE_ID = "channelUpdate"
    }

    object NotificationIds {
        const val NOTIFICATION_ADD = 1
        const val NOTIFICATION_COMPLETE = 2
        const val NOTIFICATION_UPDATE = 3
    }
}