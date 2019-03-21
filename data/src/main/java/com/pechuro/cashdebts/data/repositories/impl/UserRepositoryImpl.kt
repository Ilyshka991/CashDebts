package com.pechuro.cashdebts.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.exception.FirestoreUserNotFoundException
import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.data.model.UserBaseInformation
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val auth: IAuthRepository
) : IUserRepository {

    override val currentUserBaseInformation: UserBaseInformation
        get() = auth.getCurrentUserBaseInformation() ?: throw IllegalStateException("User not sign in")

    override fun get(uid: String) = Single.create<FirestoreUser> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .get()
            .addOnCompleteListener {
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
        .subscribeOn(Schedulers.io())

    override fun isUserExist(uid: String) = Single.create<Boolean> { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(it.result?.data != null)
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }
        .subscribeOn(Schedulers.io())

    override fun updateUser(user: FirestoreUser, uid: String) = Completable.create { emitter ->
        store.collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .set(user)
            .addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }
}
