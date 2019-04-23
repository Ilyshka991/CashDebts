package com.pechuro.cashdebts.model.notification

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.notification.NotificationConstants.DebtActionsChannelGroup.CHANNEL_ADD_ID
import com.pechuro.cashdebts.model.notification.NotificationConstants.DebtActionsChannelGroup.CHANNEL_COMPLETE_ID
import com.pechuro.cashdebts.model.notification.NotificationConstants.DebtActionsChannelGroup.CHANNEL_UPDATE_ID
import com.pechuro.cashdebts.model.notification.NotificationConstants.NotificationIds.NOTIFICATION_ADD
import com.pechuro.cashdebts.model.notification.NotificationConstants.NotificationIds.NOTIFICATION_COMPLETE
import com.pechuro.cashdebts.model.notification.NotificationConstants.NotificationIds.NOTIFICATION_UPDATE
import javax.inject.Inject

class NotificationManager @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {

    init {
        setupChannels()
    }

    fun showDebtAddNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ADD_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Title")
            .setContentText("Text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(NOTIFICATION_ADD, notification)
    }

    fun showDebtCompleteNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_COMPLETE_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Title")
            .setContentText("Text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(NOTIFICATION_COMPLETE, notification)
    }

    fun showDebtUpdateNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_UPDATE_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Title")
            .setContentText("Text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFICATION_UPDATE, notification)
    }

    private fun setupChannels() {
        createDebtActionsChannelGroup()
        createAddChannel()
        createCompleteChannel()
        createUpdateChannel()
    }

    private fun createDebtActionsChannelGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val groupId = NotificationConstants.DebtActionsChannelGroup.GROUP_ID
            val groupName = context.getString(R.string.notification_group_debt_actions)
            val channelGroup = NotificationChannelGroup(groupId, groupName)
            notificationManager.createNotificationChannelGroup(channelGroup)
        }
    }

    private fun createAddChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_add_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ADD_ID, name, importance).apply {
                group = NotificationConstants.DebtActionsChannelGroup.GROUP_ID
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createCompleteChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_complete_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_COMPLETE_ID, name, importance).apply {
                group = NotificationConstants.DebtActionsChannelGroup.GROUP_ID
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createUpdateChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_update_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_UPDATE_ID, name, importance).apply {
                group = NotificationConstants.DebtActionsChannelGroup.GROUP_ID
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}