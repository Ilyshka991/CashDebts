package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.exception.FirestoreUserNotFoundException
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.model.FirestoreUser
import com.pechuro.cashdebts.data.data.model.UserBaseInformation
import com.pechuro.cashdebts.data.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val auth: IAuthRepository
) : IUserRepository {

    override val currentUserBaseInformation: UserBaseInformation
        get() = auth.getCurrentUserBaseInformation()
            ?: throw IllegalStateException("User not sign in")

    override fun getSource(uid: String) = Observable.create<FirestoreUser> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .addSnapshotListener { snapshot, e ->
                if (emitter.isDisposed) return@addSnapshotListener
                if (e == null) {
                    if (snapshot != null && snapshot.exists()) {
                        emitter.onNext(snapshot.toObject(FirestoreUser::class.java)!!)
                    } else {
                        emitter.onError(FirestoreUserNotFoundException())
                    }
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }

    override fun getSingle(uid: String) = Single.create<FirestoreUser> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .get()
            .addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful) {
                    if (it.result?.data != null) {
                        emitter.onSuccess(it.result!!.toObject(FirestoreUser::class.java)!!)
                    } else {
                        emitter.onError(FirestoreUserNotFoundException())
                    }
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }


    override fun isUserWithUidExist(uid: String) = Single.create<Boolean> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
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

    override fun getUidByPhone(phoneNumber: String) = Single.create<String> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .whereEqualTo(FirestoreStructure.Users.Structure.phoneNumber, phoneNumber)
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

    override fun updateUser(user: FirestoreUser, uid: String) =
        Completable.create { emitter ->
            store.collection(FirestoreStructure.Users.TAG)
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
            updateRemoteDebt(uid, FirestoreStructure.RemoteDebt.Structure.debtorUid),
            updateRemoteDebt(uid, FirestoreStructure.RemoteDebt.Structure.creditorUid)
        )
    )

    private fun updateRemoteDebt(uid: String, debtRole: String) = Completable.create { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG)
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
                store.collection(FirestoreStructure.RemoteDebt.TAG)
                    .document(snapshot.id)
                    .delete()
                    .continueWithTask {
                        store.collection(FirestoreStructure.RemoteDebt.TAG)
                            .document(snapshot.id)
                            .set(snapshot.toObject(FirestoreRemoteDebt::class.java)!!)
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
