package com.pechuro.cashdebts.ui.custom.phone

import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.subjects.Subject

fun Subject<String>.receiveTextChangesFrom(view: PhoneNumberEditText) {
    view.textCode.textChanges().mergeWith(view.textNumber.textChanges()).map {
        view.getPhoneNumber()
    }.subscribe(this)
}