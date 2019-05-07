package com.pechuro.cashdebts.data.data.repositories.impl

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.pechuro.cashdebts.data.data.exception.FirebaseStorageCommonException
import com.pechuro.cashdebts.data.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.data.structure.FirebaseStorageStructure
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

internal class StorageRepositoryImpl @Inject constructor(private val storage: FirebaseStorage) : IStorageRepository {

    override fun uploadAndGetUrl(fileUri: Uri, name: String): Single<Uri> =
        Single.create<Uri> { emitter ->
        val avatarRef = storage.reference.child("${FirebaseStorageStructure.AVATARS_PATH}/$name")

        avatarRef.putFile(fileUri)
            .continueWithTask {
                if (!it.isSuccessful) {
                    emitter.onError(FirebaseStorageCommonException())
                }
                avatarRef.downloadUrl
            }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (emitter.isDisposed) {
                        delete(it.result!!.toString()).subscribe()
                        return@addOnCompleteListener
                    }
                    emitter.onSuccess(it.result!!)
                } else {
                    emitter.onError(FirebaseStorageCommonException())
                }
            }
    }

    override fun delete(url: String): Completable = Completable.create { emitter ->
        val ref = storage.getReferenceFromUrl(url)
        ref.delete().addOnCompleteListener {
            if (emitter.isDisposed) return@addOnCompleteListener
            if (it.isSuccessful) {
                emitter.onComplete()
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }
}