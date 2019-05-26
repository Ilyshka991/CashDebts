package com.pechuro.cashdebts.service.notification

import android.app.Service
import android.content.Context
import android.content.Intent
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.repositories.IRemoteDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.notification.NotificationConstants
import com.pechuro.cashdebts.model.notification.NotificationManager
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class NotificationCreateActionService : Service() {

    @Inject
    protected lateinit var remoteDebtRepository: IRemoteDebtRepository
    @Inject
    protected lateinit var notificationManager: NotificationManager
    @Inject
    protected lateinit var disposable: CompositeDisposable
    @Inject
    protected lateinit var userRepository: IUserRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY
        val notificationId = intent.getIntExtra(INTENT_ARG_NOTIFICATION_ID, -1)
        notificationManager.dismiss(notificationId)
        val action = intent.getIntExtra(INTENT_ARG_ACTION, NotificationConstants.Action.ADD_REJECT)
        val debtId = intent.getStringExtra(INTENT_ARG_DEBT_ID)
        performWork(action, debtId)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun performWork(
        action: Int,
        debtId: String
    ) {
        remoteDebtRepository.getSingle(debtId).flatMapCompletable {
            remoteDebtRepository.update(debtId, getDebt(action, it))
        }.subscribe({
            stopSelf()
        }, {
            stopSelf()
        }).addTo(disposable)
    }

    private fun getDebt(
        action: Int,
        debt: FirestoreRemoteDebt
    ): FirestoreRemoteDebt {
        val status = when (action) {
            NotificationConstants.Action.ADD_ACCEPT -> FirestoreDebtStatus.IN_PROGRESS
            NotificationConstants.Action.ADD_REJECT -> FirestoreDebtStatus.CONFIRMATION_REJECTED
            else -> throw IllegalArgumentException()
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
                isFirstTimeAdded,
                userRepository.currentUserBaseInformation.uid
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
            action: Int,
            debtId: String,
            notificationId: Int
        ) =
            Intent(context, NotificationCreateActionService::class.java).apply {
                putExtra(INTENT_ARG_ACTION, action)
                putExtra(INTENT_ARG_DEBT_ID, debtId)
                putExtra(INTENT_ARG_NOTIFICATION_ID, notificationId)
            }
    }
}