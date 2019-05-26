package com.pechuro.cashdebts.model.notification

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.notification.NotificationConstants.DebtActionsChannelGroup.CHANNEL_ADD_ID
import com.pechuro.cashdebts.model.notification.NotificationConstants.DebtActionsChannelGroup.CHANNEL_UPDATE_ID
import com.pechuro.cashdebts.model.notification.NotificationConstants.NotificationIds.NOTIFICATION_UPDATE
import com.pechuro.cashdebts.service.notification.NotificationCreateActionService
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import javax.inject.Inject
import kotlin.random.Random

class NotificationManager @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {

    init {
        setupChannels()
    }

    fun show(data: Map<String, String>) {
        when (data[NotificationStructure.CommonStructure.TYPE]) {
            NotificationStructure.Types.CREATE -> {
                val id = data[NotificationStructure.CreateStructure.ID]
                    ?: throw IllegalArgumentException("Id must be specified")
                val personName =
                    data[NotificationStructure.CreateStructure.PERSON_NAME]
                        ?: throw IllegalArgumentException("Value must be specified")
                val value = data[NotificationStructure.CreateStructure.VALUE]?.toDoubleOrNull()
                    ?: throw IllegalArgumentException("Value must be specified")
                showDebtAddNotification(NotificationCreateData(id, personName, value))
            }
            NotificationStructure.Types.UPDATE -> {
                showDebtUpdateNotification()
            }
        }
    }

    fun dismiss(id: Int) {
        notificationManager.cancel(id)
    }

    private fun showDebtAddNotification(data: NotificationCreateData) {
        val tapPendingIntent = MainActivity.newIntent(context).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.run { PendingIntent.getActivity(context, 0, this, 0) }

        val acceptPendingIntent = NotificationCreateActionService.newIntent(
            context,
            NotificationConstants.Action.ADD_ACCEPT,
            data.id,
            data.hashCode()
        ).run {
            PendingIntent.getService(
                context,
                Random.nextInt(),
                this,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val rejectPendingIntent = NotificationCreateActionService.newIntent(
            context,
            NotificationConstants.Action.ADD_REJECT,
            data.id,
            data.hashCode()
        ).run {
            PendingIntent.getService(
                context,
                Random.nextInt(),
                this,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val text = if (data.value > 0) {
            context.getString(
                R.string.notification_create_text_creditor,
                data.personName,
                data.value
            )
        } else {
            context.getString(
                R.string.notification_create_text_debtor,
                data.personName,
                -data.value
            )
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ADD_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notification_create_title))
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .setGroup(NotificationConstants.Group.ID_CREATE_GROUP)
            .addAction(
                R.drawable.ic_delete,
                context.getString(R.string.notification_create_action_reject),
                rejectPendingIntent
            )
            .addAction(
                R.drawable.ic_done,
                context.getString(R.string.notification_create_action_accept),
                acceptPendingIntent
            )
            .build()

        notificationManager.notify(data.hashCode(), notification)
    }

    private fun showDebtUpdateNotification() {
        val tapPendingIntent = MainActivity.newIntent(context).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.run { PendingIntent.getActivity(context, 0, this, 0) }

        val notification = NotificationCompat.Builder(context, CHANNEL_UPDATE_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notification_update_title))
            .setContentText(context.getString(R.string.notification_update_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_UPDATE, notification)
    }

    private fun setupChannels() {
        createDebtActionsChannelGroup()
        createAddChannel()
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
            val name = context.getString(R.string.notification_channel_add)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ADD_ID, name, importance).apply {
                group = NotificationConstants.DebtActionsChannelGroup.GROUP_ID
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createUpdateChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_update)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_UPDATE_ID, name, importance).apply {
                group = NotificationConstants.DebtActionsChannelGroup.GROUP_ID
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}