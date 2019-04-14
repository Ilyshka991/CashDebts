package com.pechuro.cashdebts.ui.custom.hintedittext

import com.pechuro.cashdebts.ui.custom.TextWatcher
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

fun Subject<String>.receiveTextChangesFrom(view: HintEditText): Disposable {
    val listener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onNext(view.getEnteredText())
        }
    }
    view.addTextChangedListener(listener)

    return doOnDispose {
        view.removeTextChangedListener(listener)
    }
        .filter { view.text.toString() != it }
        .subscribe {
            view.setText(it)
            view.setSelection(it.length)
        }
}