package com.pechuro.cashdebts.ui.utils.binding

import android.widget.EditText
import com.pechuro.cashdebts.ui.custom.TextWatcher
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject
import java.text.SimpleDateFormat
import java.util.*

fun Subject<String>.receiveTextChangesFrom(view: EditText): Disposable {
    val listener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onNext(s.toString())
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

fun Subject<Date>.receiveDateChangesFrom(view: EditText, formatter: SimpleDateFormat): Disposable {
    val listener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onNext(formatter.parse(s.toString()))
        }
    }
    view.addTextChangedListener(listener)

    return doOnDispose {
        view.removeTextChangedListener(listener)
    }.map(formatter::format)
        .filter { view.text.toString() != it }
        .subscribe {
            view.setText(it)
            view.setSelection(it.length)
        }
}