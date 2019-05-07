package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.pechuro.cashdebts.data.data.model.FirestoreToken
import com.pechuro.cashdebts.data.data.repositories.IMessagingRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

internal class MessagingRepositoryImpl @Inject constructor(
    private val messagingClient: FirebaseMessaging,
    private val instanceId: FirebaseInstanceId,
    private val firestore: FirebaseFirestore
) : IMessagingRepository {

    override fun setEnabled(isEnabled: Boolean) {
        messagingClient.isAutoInitEnabled = isEnabled
    }

    override fun getCurrentToken() = Single.create<String> { emitter ->
        instanceId.instanceId
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    val token = task.result?.token
                    if (task.isSuccessful && token != null) {
                        emitter.onSuccess(token)
                    } else {
                        emitter.onError(task.exception ?: Exception())
                    }
                }
            }
    }

    override fun deleteCurrentToken(personUid: String) = Completable.create { emitter ->
        firestore.collection(FirestoreStructure.Tokens.TAG)
            .document(personUid)
            .collection(FirestoreStructure.Tokens.Structure.PushTokens.TAG)
            .document(instanceId.id)
            .delete()
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception ?: Exception())
                    }
                }
            }
    }

    override fun saveToken(personUid: String, token: String?) = if (token == null) {
        getCurrentToken().flatMapCompletable {
            saveTokenToFirestore(personUid, instanceId.id, it)
        }
    } else {
        saveTokenToFirestore(personUid, instanceId.id, token)
    }

    private fun saveTokenToFirestore(personUid: String, instanceId: String, token: String) =
        Completable.create { emitter ->
            firestore.collection(FirestoreStructure.Tokens.TAG)
                .document(personUid)
                .collection(FirestoreStructure.Tokens.Structure.PushTokens.TAG)
                .document(instanceId)
                .set(FirestoreToken(token))
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception ?: Exception())
                        }
                    }
                }
        }
}