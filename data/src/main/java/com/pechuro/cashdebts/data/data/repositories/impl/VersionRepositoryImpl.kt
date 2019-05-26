package com.pechuro.cashdebts.data.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.data.exception.FirestoreCommonException
import com.pechuro.cashdebts.data.data.repositories.IVersionRepository
import com.pechuro.cashdebts.data.data.structure.FirestoreStructure
import io.reactivex.Observable
import javax.inject.Inject

internal class VersionRepositoryImpl @Inject constructor(
    private val store: FirebaseFirestore
) : IVersionRepository {

    override fun getCurrentVersion(): Observable<Long> = Observable.create { emitter ->
        store.collection(FirestoreStructure.TAG_VERSION)
            .document(FirestoreStructure.TAG_VERSION)
            .addSnapshotListener { snapshot, e ->
                if (emitter.isDisposed) return@addSnapshotListener
                if (e == null) {
                    val version = snapshot?.getLong(FirestoreStructure.TAG_VERSION)
                    if (version != null) {
                        emitter.onNext(version)
                    }
                } else {
                    emitter.onError(FirestoreCommonException())
                }

            }
    }
}
