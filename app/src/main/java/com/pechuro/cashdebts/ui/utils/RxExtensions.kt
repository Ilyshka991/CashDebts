package com.pechuro.cashdebts.ui.utils

import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.subjects.Subject

fun Subject<String>.receiveTextChangesFrom(view: EditText) {
    view.textChanges().map(CharSequence::toString).subscribe(this)
}

fun Subject<String>.receiveQueryChangesFrom(view: SearchView) {
    view.queryTextChanges().map(CharSequence::toString).subscribe(this)
}