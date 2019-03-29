package com.pechuro.cashdebts.ui.utils

import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*

fun Subject<String>.receiveTextChangesFrom(view: EditText) {
    view.textChanges().skipInitialValue().map(CharSequence::toString).subscribe(this)
}

fun Subject<String>.receiveQueryChangesFrom(view: SearchView) {
    view.queryTextChanges().map(CharSequence::toString).subscribe(this)
}

fun Subject<Double>.receiveDecimalChangesFrom(view: EditText) {
    view.textChanges().skipInitialValue().map(CharSequence::toString).map(String::toDouble).subscribe(this)
}

fun Subject<Date>.receiveDateChangesFrom(view: EditText) {
    view.textChanges().skipInitialValue().map(CharSequence::toString).map(::Date).subscribe(this)
}

inline val <T : Any> BehaviorSubject<T>.requireValue: T
    get() = value ?: throw NullPointerException()

fun Subject<Int>.receiveDebtRole(view: ChipGroup) {
    view.setOnCheckedChangeListener { chipGroup, i ->
        println("AAAAAAAAAAAAAAAAAAAAAAAAA")
    }
}