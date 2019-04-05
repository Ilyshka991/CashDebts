package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val auth: IAuthRepository
) : IUserRepository {

    override val currentUserBaseInformation: com.pechuro.cashdebts.data.data.model.UserBaseInformation
        get() = auth.getCurrentUserBaseInformation() ?: throw IllegalStateException("User not sign in")

    override fun get(uid: String) = Single.create<com.pechuro.cashdebts.data.data.model.FirestoreUser> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result?.data != null) {
                        emitter.onSuccess(it.result!!.toObject(com.pechuro.cashdebts.data.data.model.FirestoreUser::class.java)!!)
                    } else {
                        emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreUserNotFoundException())
                    }
                } else {
                    emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreCommonException())
                }
            }
    }

    override fun isUserWithUidExist(uid: String) = Single.create<Boolean> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(it.result?.data != null)
                } else {
                    emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreCommonException())
                }
            }
    }

    override fun getUidByPhone(phoneNumber: String) = Single.create<String> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .whereEqualTo(FirestoreStructure.Users.Structure.phoneNumber, phoneNumber)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result?.documents?.size == 1) {
                        emitter.onSuccess(it.result!!.documents[0]!!.id)
                    } else {
                        emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreUserNotFoundException())
                    }
                } else {
                    emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreCommonException())
                }
            }
    }

    override fun updateUser(user: com.pechuro.cashdebts.data.data.model.FirestoreUser, uid: String) =
        Completable.create { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .set(user)
            .addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(com.pechuro.cashdebts.data.data.exception.FirestoreCommonException())
                }
            }
    }
}
