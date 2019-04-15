package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.model.FirestoreRemoteDebt
import com.pechuro.cashdebts.data.data.repositories.IRemoteDebtRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.creditorUid
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure.RemoteDebt.Structure.debtorUid
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*
import javax.inject.Inject

internal class RemoteDebtRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore,
    private val userRepository: IUserRepository
) : IRemoteDebtRepository {

    override fun getSource() =
        Observable.zip(Observable.create<Map<String, FirestoreRemoteDebt>> { emitter ->
            store.collection(FirestoreStructure.RemoteDebt.TAG)
                .whereEqualTo(creditorUid, userRepository.currentUserBaseInformation.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                    if (snapshot == null) return@addSnapshotListener
                    snapshot.documents
                        .mapNotNull {
                            it.id to it.toObject(FirestoreRemoteDebt::class.java)!!
                        }
                        .toMap()
                        .let {
                            if (!emitter.isDisposed) emitter.onNext(it)
                        }
                }
        }, Observable.create<Map<String, FirestoreRemoteDebt>> { emitter ->
            store.collection(FirestoreStructure.RemoteDebt.TAG)
                .whereEqualTo(debtorUid, userRepository.currentUserBaseInformation.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                    if (snapshot == null) return@addSnapshotListener
                    snapshot.documents
                        .mapNotNull {
                            it.id to it.toObject(FirestoreRemoteDebt::class.java)!!
                        }
                        .toMap()
                        .let {
                            if (!emitter.isDisposed) emitter.onNext(it)
                        }
                }
        }, BiFunction { creditor: Map<String, FirestoreRemoteDebt>, debtor: Map<String, FirestoreRemoteDebt> ->
            creditor to debtor
        }).map {
            it.first + it.second
        }

    override fun get(id: String): Single<FirestoreRemoteDebt> {
        return Single.just(FirestoreRemoteDebt("", "", 0.4, "", Date(), 3))
    }

    override fun add(debt: FirestoreRemoteDebt, id: String?) = Completable.create { emitter ->
        store.collection(FirestoreStructure.RemoteDebt.TAG)
            .add(debt).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(FirestoreCommonException())
                }
            }
    }
}