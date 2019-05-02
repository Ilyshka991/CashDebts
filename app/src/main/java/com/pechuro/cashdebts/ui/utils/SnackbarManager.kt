package com.pechuro.cashdebts.ui.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import io.reactivex.subjects.PublishSubject

class SnackActionInfo(@StringRes val actionId: Int, val callback: () -> Unit)

class SnackInfo(
    @StringRes val msgId: Int,
    val duration: Int = Snackbar.LENGTH_LONG,
    val action: SnackActionInfo? = null
)

object SnackbarManager {
    private val emitter = PublishSubject.create<SnackInfo>()

    fun show(info: SnackInfo) {
        emitter.onNext(info)
    }

    fun listen(info: (SnackInfo) -> Unit) = emitter.subscribe(info)
}