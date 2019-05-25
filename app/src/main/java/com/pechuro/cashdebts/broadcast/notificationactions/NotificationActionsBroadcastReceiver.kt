package com.pechuro.cashdebts.broadcast.notificationactions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.model.notification.NotificationConstants

class NotificationActionsBroadcastReceiver : BroadcastReceiver() {
    /*  @Inject
      protected lateinit var remoteDebtRepository: IRemoteDebtRepository
  */
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "AAAAAAAAAAAAAA", Toast.LENGTH_LONG).show()
        val action = intent.getSerializableExtra(INTENT_ARG_ACTION) as NotificationConstants.Action
        val debtId = intent.getStringExtra(INTENT_ARG_ID)
        /*remoteDebtRepository.getSingle(debtId).flatMapCompletable {
            remoteDebtRepository.update(debtId, getDebt(action, it))
        }.subscribe({
            println("AAAA")
        }, {
            println("VVVVVV")
        })*/
    }

    private fun getDebt(
        action: NotificationConstants.Action,
        debt: FirestoreRemoteDebt
    ): FirestoreRemoteDebt {
        val status = when (action) {
            NotificationConstants.Action.ADD_ACCEPT -> FirestoreDebtStatus.IN_PROGRESS
            NotificationConstants.Action.ADD_REJECT -> FirestoreDebtStatus.CONFIRMATION_REJECTED
        }
        return with(debt) {
            FirestoreRemoteDebt(
                creditorUid,
                debtorUid,
                value,
                description,
                date,
                status,
                initPersonUid,
                deleteStatus,
                isFirstTimeAdded
            )
        }
    }

    companion object {
        private const val INTENT_ARG_ACTION = "action"
        private const val INTENT_ARG_ID = "arg_id"

        fun newIntent(
            context: Context,
            action: NotificationConstants.Action,
            debtId: String
        ) =
            Intent(context, NotificationActionsBroadcastReceiver::class.java).apply {
                putExtra(INTENT_ARG_ACTION, action)
                putExtra(INTENT_ARG_ID, debtId)
            }
    }
}