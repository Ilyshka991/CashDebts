package com.pechuro.cashdebts.ui.utils

import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.widget.textChanges
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.model.DebtRole
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

fun Subject<Int>.receiveDebtRole(view: ChipGroup) {
    val listener = ChipGroup.OnCheckedChangeListener { _, id ->
        for (i in 0 until view.childCount) {
            val chip = view.getChildAt(i)
            chip.isClickable = chip.id != id
        }
        val role = when (id) {
            R.id.chip_creditor -> DebtRole.CREDITOR
            R.id.chip_debtor -> DebtRole.DEBTOR
            else -> throw IllegalArgumentException()
        }
        onNext(role)
    }
    view.setOnCheckedChangeListener(listener)
}