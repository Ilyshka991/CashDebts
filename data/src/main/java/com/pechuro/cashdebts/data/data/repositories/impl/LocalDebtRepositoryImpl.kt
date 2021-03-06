package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.data.data.repositories.ILocalDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

internal class LocalDebtRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val userRepository: IUserRepository
) : ILocalDebtRepository {

    override fun getSource(): Observable<Map<String, FirestoreLocalDebt>> =
        Observable.create<Map<String, FirestoreLocalDebt>> { emitter ->
            store.collection(FirestoreStructure.TAG_LOCAL_DEBTS)
                .whereEqualTo(
                    FirestoreLocalDebt::ownerUid.name,
                    userRepository.currentUserBaseInformation.uid
                )
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot == null) return@addSnapshotListener
                    snapshot.documents
                        .mapNotNull {
                            val debt = with(it) {
                                FirestoreLocalDebt(
                                    getString(FirestoreLocalDebt::ownerUid.name) ?: "",
                                    getString(FirestoreLocalDebt::name.name) ?: "",
                                    getDouble(FirestoreLocalDebt::value.name) ?: 0.0,
                                    getString(FirestoreLocalDebt::description.name) ?: "",
                                    getDate(FirestoreLocalDebt::date.name) ?: Date(),
                                    getLong(FirestoreLocalDebt::role.name)?.toInt()
                                        ?: DebtRole.CREDITOR
                                )
                            }
                            it.id to debt
                        }
                        .toMap()
                        .let {
                            if (!emitter.isDisposed) emitter.onNext(it)
                        }
                }
        }

    override fun getSingle(id: String): Single<FirestoreLocalDebt> =
        Single.create<FirestoreLocalDebt> { emitter ->
            store.collection(FirestoreStructure.TAG_LOCAL_DEBTS).document(id).get()
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        if (!emitter.isDisposed) {
                            val debt = with(it.result!!) {
                                FirestoreLocalDebt(
                                    getString(FirestoreLocalDebt::ownerUid.name) ?: "",
                                    getString(FirestoreLocalDebt::name.name) ?: "",
                                    getDouble(FirestoreLocalDebt::value.name) ?: 0.0,
                                    getString(FirestoreLocalDebt::description.name) ?: "",
                                    getDate(FirestoreLocalDebt::date.name) ?: Date(),
                                    getLong(FirestoreLocalDebt::role.name)?.toInt()
                                        ?: DebtRole.CREDITOR
                                )
                            }
                            emitter.onSuccess(debt)
                        }
                    } else {
                        if (!emitter.isDisposed) {
                            emitter.onError(FirestoreCommonException())
                        }
                    }
                }
        }

    override fun add(debt: FirestoreLocalDebt): Completable = Completable.create { emitter ->
        store.collection(FirestoreStructure.TAG_LOCAL_DEBTS)
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
            store.collection(FirestoreStructure.TAG_LOCAL_DEBTS)
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
        store.collection(FirestoreStructure.TAG_LOCAL_DEBTS).document(id).delete()
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