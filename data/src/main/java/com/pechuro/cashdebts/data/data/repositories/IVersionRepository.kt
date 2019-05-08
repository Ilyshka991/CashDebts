package com.pechuro.cashdebts.data.data.repositories

import io.reactivex.Observable

interface IVersionRepository {
    fun getCurrentVersion(): Observable<Long>
}