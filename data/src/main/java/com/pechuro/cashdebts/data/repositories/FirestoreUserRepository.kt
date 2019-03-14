package com.pechuro.cashdebts.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.exception.FirestoreUserNotFoundException
import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FirestoreUserRepository @Inject constructor(private val store: FirebaseFirestore) {

    fun get(uid: String) = Single.create<FirestoreUser> { emitter ->
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

    fun isUserExist(uid: String) = Single.create<Boolean> { emitter ->
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

    fun setUser(uid: String, user: FirestoreUser) = Completable.create { emitter ->
        FirebaseFirestore.getInstance().collection(FirestoreStructure.Users.TAG)
            .document(uid)
            .set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }

            }
    }
}
