package com.pechuro.cashdebts.data.data.repositories

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single

interface IStorageRepository {
    fun uploadAndGetUrl(fileUri: Uri, name: String): Single<Uri>

    fun deletePrevious(url: String): Completable
}