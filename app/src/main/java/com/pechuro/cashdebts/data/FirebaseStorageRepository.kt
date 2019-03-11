package com.pechuro.cashdebts.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.pechuro.cashdebts.data.exception.FirebaseStorageCommonException
import com.pechuro.cashdebts.data.structure.FirebaseStorageStructure
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FirebaseStorageRepository(private val storage: FirebaseStorage) {

    fun uploadAndGetUrl(fileUri: Uri, name: String) = Single.create<Uri> { emitter ->
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
                    emitter.onSuccess(it.result!!)
                } else {
                    emitter.onError(FirebaseStorageCommonException())
                }
            }
    }
        .subscribeOn(Schedulers.io())
}