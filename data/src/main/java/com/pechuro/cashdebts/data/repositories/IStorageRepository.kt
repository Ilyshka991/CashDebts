package com.pechuro.cashdebts.data.repositories

import android.net.Uri
import io.reactivex.Single

interface IStorageRepository {
    fun uploadAndGetUrl(fileUri: Uri, name: String): Single<Uri>
}