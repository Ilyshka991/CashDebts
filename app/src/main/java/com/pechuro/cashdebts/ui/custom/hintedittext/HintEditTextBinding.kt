package com.pechuro.cashdebts.ui.custom.hintedittext

import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.subjects.Subject

fun Subject<String>.receiveTextChangesFrom(view: HintEditText) {
    view.textChanges().map { view.getEnteredText() }.subscribe(this)
}