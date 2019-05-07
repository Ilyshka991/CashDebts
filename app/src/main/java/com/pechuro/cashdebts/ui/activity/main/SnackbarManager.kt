package com.pechuro.cashdebts.ui.activity.main

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class SnackActionInfo(@StringRes val actionId: Int, val callback: () -> Unit)

class SnackInfo(
    @StringRes val msgId: Int,
    val duration: Int = Snackbar.LENGTH_LONG,
    val action: SnackActionInfo? = null
) {
    fun isEmpty() = msgId == 0

    companion object {
        val EMPTY = SnackInfo(0)
    }
}

object SnackbarManager {
    private val emitter = PublishSubject.create<SnackInfo>()

    fun show(info: SnackInfo) {
        emitter.onNext(info)
    }

    fun listen(info: (SnackInfo) -> Unit): Disposable = emitter.subscribe(info)
}