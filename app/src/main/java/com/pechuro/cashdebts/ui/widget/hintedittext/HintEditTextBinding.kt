package com.pechuro.cashdebts.ui.widget.hintedittext

import com.pechuro.cashdebts.ui.widget.TextWatcher
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

fun Subject<String>.receiveTextChangesFrom(view: HintEditText): Disposable {
    val listener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            view.getEnteredText()?.let { onNext(it) }
        }
    }
    view.addTextChangedListener(listener)

    return doOnDispose {
        view.removeTextChangedListener(listener)
    }
        .filter { view.getEnteredText() != it }
        .subscribe {
            view.setText(it)
            view.setSelection(it.length)
        }
}