package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.exception.FirestoreUserNotFoundException
import com.pechuro.cashdebts.data.data.model.*
import com.pechuro.cashdebts.data.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val auth: IAuthRepository
) : IUserRepository {

    private val userSnapshotListeners = mutableListOf<ListenerRegistration>()

    override val currentUserBaseInformation: UserBaseInformation
        get() = auth.getCurrentUserBaseInformation()
            ?: throw IllegalStateException("User not sign in")

    override fun getSource(uid: String): Observable<FirestoreUser> =
        Observable.create<FirestoreUser> { emitter ->
            store.collection(FirestoreStructure.TAG_USERS)
                .document(uid)
                .addSnapshotListener { snapshot, e ->
                    if (emitter.isDisposed) return@addSnapshotListener
                    if (e == null) {
                        if (snapshot != null && snapshot.exists()) {
                            val user = with(snapshot) {
                                FirestoreUser(
                                    getString(FirestoreUser::firstName.name) ?: "",
                                    getString(FirestoreUser::lastName.name) ?: "",
                                    getString(FirestoreUser::phoneNumber.name) ?: "",
                                    getString(FirestoreUser::photoUrl.name)
                                )
                            }
                            emitter.onNext(user)
                        } else {
                            emitter.onError(FirestoreUserNotFoundException())
                        }
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }.also {
                    userSnapshotListeners += it
                }
        }.doOnDispose {
            userSnapshotListeners.forEach {
                it.remove()
            }
        }

    override fun getSingle(uid: String, fromCache: Boolean): Single<FirestoreUser> =
        Single.create<FirestoreUser> { emitter ->
            store.collection(FirestoreStructure.TAG_USERS)
                .document(uid)
                .get(if (fromCache) Source.CACHE else Source.DEFAULT)
                .addOnCompleteListener {
                    if (emitter.isDisposed) return@addOnCompleteListener
                    if (it.isSuccessful) {
                        if (it.result?.data != null) {
                            val user = with(it.result!!) {
                                FirestoreUser(
                                    getString(FirestoreUser::firstName.name) ?: "",
                                    getString(FirestoreUser::lastName.name) ?: "",
                                    getString(FirestoreUser::phoneNumber.name) ?: "",
                                    getString(FirestoreUser::photoUrl.name)
                                )
                            }
                            emitter.onSuccess(user)
                        } else {
                            emitter.onError(FirestoreUserNotFoundException())
                        }
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }
        }

    override fun isUserWithUidExist(uid: String): Single<Boolean> =
        Single.create<Boolean> { emitter ->
            store.collection(FirestoreStructure.TAG_USERS)
                .document(uid)
                .get()
                .addOnCompleteListener {
                    if (emitter.isDisposed) return@addOnCompleteListener
                    if (it.isSuccessful) {
                        emitter.onSuccess(it.result?.data != null)
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }
        }

    override fun getUidByPhone(phoneNumber: String): Single<String> =
        Single.create<String> { emitter ->
            store.collection(FirestoreStructure.TAG_USERS)
                .whereEqualTo(FirestoreUser::phoneNumber.name, phoneNumber)
                .get()
                .addOnCompleteListener {
                    if (emitter.isDisposed) return@addOnCompleteListener
                    if (it.isSuccessful) {
                        if (it.result?.documents?.size == 1) {
                            emitter.onSuccess(it.result!!.documents[0]!!.id)
                        } else {
                            emitter.onError(FirestoreUserNotFoundException())
                        }
                    } else {
                        emitter.onError(FirestoreCommonException())
                    }
                }
        }

    override fun updateUser(user: FirestoreUser, uid: String): Completable =
        Completable.create { emitter ->
            store.collection(FirestoreStructure.TAG_USERS)
                .document(uid)
                .set(user)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        updateRemoteDebts(uid).subscribe({
                            if (!emitter.isDisposed) emitter.onComplete()
                        }, {
                            if (!emitter.isDisposed) emitter.onError(FirestoreCommonException())
                        })
                    } else {
                        if (!emitter.isDisposed) emitter.onError(FirestoreCommonException())
                    }
                }
        }

    private fun updateRemoteDebts(uid: String) = Completable.merge(
        listOf(
            updateRemoteDebt(uid, FirestoreRemoteDebt::debtorUid.name),
            updateRemoteDebt(uid, FirestoreRemoteDebt::creditorUid.name)
        )
    )

    private fun updateRemoteDebt(uid: String, debtRole: String) = Completable.create { emitter ->
        store.collection(FirestoreStructure.TAG_REMOTE_DEBTS)
            .whereEqualTo(debtRole, uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateDocuments(task.result!!).subscribe({
                        if (!emitter.isDisposed) emitter.onComplete()
                    }, {
                        if (!emitter.isDisposed) emitter.onError(FirestoreCommonException())
                    })
                } else {
                    if (!emitter.isDisposed) emitter.onError(FirestoreCommonException())
                }
            }
    }

    private fun updateDocuments(snapshot: QuerySnapshot) = Completable.create { emitter ->
        if (snapshot.documents.isNotEmpty()) {
            snapshot.documents.forEach { snapshot ->
                store.collection(FirestoreStructure.TAG_REMOTE_DEBTS)
                    .document(snapshot.id)
                    .delete()
                    .continueWithTask {
                        val debt = with(snapshot) {
                            FirestoreRemoteDebt(
                                getString(FirestoreRemoteDebt::creditorUid.name)
                                    ?: "",
                                getString(FirestoreRemoteDebt::debtorUid.name) ?: "",
                                getDouble(FirestoreRemoteDebt::value.name) ?: 0.0,
                                getString(FirestoreRemoteDebt::description.name)
                                    ?: "",
                                getDate(FirestoreRemoteDebt::date.name) ?: Date(),
                                getLong(FirestoreRemoteDebt::status.name)?.toInt()
                                    ?: FirestoreDebtStatus.NOT_SEND,
                                getString(FirestoreRemoteDebt::initPersonUid.name)
                                    ?: "",
                                getLong(FirestoreRemoteDebt::deleteStatus.name)?.toInt()
                                    ?: DebtDeleteStatus.NOT_DELETED

                            )
                        }
                        store.collection(FirestoreStructure.TAG_REMOTE_DEBTS)
                            .document(snapshot.id)
                            .set(debt)
                    }
                    .addOnCompleteListener { task ->
                        if (emitter.isDisposed) return@addOnCompleteListener
                        if (task.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(FirestoreCommonException())
                        }
                    }
            }
        } else {
            if (!emitter.isDisposed) emitter.onComplete()
        }
    }
}
