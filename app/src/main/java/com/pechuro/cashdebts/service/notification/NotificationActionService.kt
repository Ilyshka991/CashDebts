package com.pechuro.cashdebts.service.notification

import android.app.Service
import android.content.Context
import android.content.Intent
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.repositories.IRemoteDebtRepository
import com.pechuro.cashdebts.model.notification.NotificationConstants
import com.pechuro.cashdebts.model.notification.NotificationManager
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class NotificationActionService : Service() {

    @Inject
    protected lateinit var remoteDebtRepository: IRemoteDebtRepository
    @Inject
    protected lateinit var notificationManager: NotificationManager
    @Inject
    protected lateinit var disposable: CompositeDisposable

    override fun onCreate() {
        AndroidInjection.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY
        val action = intent.getSerializableExtra(INTENT_ARG_ACTION) as NotificationConstants.Action
        val debtId = intent.getStringExtra(INTENT_ARG_DEBT_ID)
        val notificationId = intent.getIntExtra(INTENT_ARG_NOTIFICATION_ID, -1)
        println(notificationId)
        performWork(action, debtId, notificationId)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun performWork(
        action: NotificationConstants.Action,
        debtId: String,
        notificationId: Int
    ) {
        remoteDebtRepository.getSingle(debtId).flatMapCompletable {
            remoteDebtRepository.update(debtId, getDebt(action, it))
        }.subscribe({
            notificationManager.dismiss(notificationId)
            stopSelf()
        }, {
            notificationManager.dismiss(notificationId)
            stopSelf()
        }).addTo(disposable)
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

    override fun onBind(intent: Intent?) = null

    companion object {
        private const val INTENT_ARG_ACTION = "action"
        private const val INTENT_ARG_DEBT_ID = "arg_debt_id"
        private const val INTENT_ARG_NOTIFICATION_ID = "arg_notification_id"

        fun newIntent(
            context: Context,
            action: NotificationConstants.Action,
            debtId: String,
            notificationId: Int
        ) =
            Intent(context, NotificationActionService::class.java).apply {
                putExtra(INTENT_ARG_ACTION, action)
                putExtra(INTENT_ARG_DEBT_ID, debtId)
                putExtra(INTENT_ARG_NOTIFICATION_ID, notificationId)
            }
    }
}