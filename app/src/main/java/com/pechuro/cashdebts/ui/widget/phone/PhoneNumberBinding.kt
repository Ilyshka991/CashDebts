package com.pechuro.cashdebts.ui.widget.phone

import com.pechuro.cashdebts.ui.widget.TextWatcher
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

fun Subject<String>.receiveTextChangesFrom(view: PhoneNumberEditText): Disposable {
    val codeListener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onNext(view.getPhoneNumber())
        }
    }
    val numberListener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onNext(view.getPhoneNumber())
        }
    }
    view.textCode.addTextChangedListener(codeListener)
    view.textNumber.addTextChangedListener(numberListener)

    return doOnDispose {
        view.textCode.removeTextChangedListener(codeListener)
        view.textNumber.removeTextChangedListener(numberListener)
    }
        .filter {
            it != view.getPhoneNumber()
        }
        .subscribe {
            view.setPhoneNumber(it)
        }
}