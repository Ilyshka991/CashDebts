package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.repositories.ILocalDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

internal class LocalDebtRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val userRepository: IUserRepository
) : ILocalDebtRepository {

    override fun getSource(): Observable<Map<String, FirestoreLocalDebt>> =
        Observable.create<Map<String, FirestoreLocalDebt>> { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG)
            .whereEqualTo(
                FirestoreStructure.LocalDebt.Structure.ownerUid,
                userRepository.currentUserBaseInformation.uid
            )
            .addSnapshotListener { snapshot, exception ->
                if (snapshot == null) return@addSnapshotListener
                snapshot.documents
                    .mapNotNull {
                        it.id to it.toObject(FirestoreLocalDebt::class.java)!!
                    }
                    .toMap()
                    .let {
                        if (!emitter.isDisposed) emitter.onNext(it)
                    }
            }
    }

    override fun getSingle(id: String): Single<FirestoreLocalDebt> =
        Single.create<FirestoreLocalDebt> { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG).document(id).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    if (!emitter.isDisposed) {
                        emitter.onSuccess(it.result!!.toObject(FirestoreLocalDebt::class.java)!!)
                    }
                } else {
                    if (!emitter.isDisposed) {
                        emitter.onError(FirestoreCommonException())
                    }
                }
            }
    }

    override fun add(debt: FirestoreLocalDebt): Completable = Completable.create { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG)
            .add(debt).addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }

    override fun update(id: String, debt: FirestoreLocalDebt): Completable =
        Completable.create { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG)
            .document(id)
            .set(debt).addOnCompleteListener {
                if (emitter.isDisposed) return@addOnCompleteListener
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }

    override fun delete(id: String): Completable = Completable.create { emitter ->
        store.collection(FirestoreStructure.LocalDebt.TAG).document(id).delete()
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