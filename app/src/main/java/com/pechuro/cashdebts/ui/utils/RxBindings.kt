package com.pechuro.cashdebts.ui.utils

import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.chip.ChipGroup.OnCheckedChangeListener
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.widget.textChanges
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.DebtRole
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import java.text.SimpleDateFormat
import java.util.*

fun Subject<String>.receiveTextChangesFrom(view: EditText) {
    view.textChanges().skipInitialValue().map(CharSequence::toString).subscribe(this)
        .also {
            this.distinctUntilChanged()
                .filter { view.text.toString() != it }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.setText(it)
                    view.setSelection(it.length)
                }
        }
}

fun Subject<String>.receiveQueryChangesFrom(view: SearchView) {
    view.queryTextChanges().skipInitialValue().skip(1).map(CharSequence::toString).subscribe(this)
}

fun Subject<Double>.receiveDecimalChangesFrom(view: EditText) {
    view.textChanges().skipInitialValue().map(CharSequence::toString).map {
        try {
            it.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }.subscribe(this)
}

fun Subject<Date>.receiveDateChangesFrom(view: EditText, formatter: SimpleDateFormat) {
    view.textChanges().skipInitialValue().map(CharSequence::toString).map(formatter::parse).subscribe(this)
}

fun Subject<Int>.receiveDebtRole(view: ChipGroup) {
    view.findViewById<View>(view.checkedChipId).isClickable = false
    val listener = OnCheckedChangeListener { _, id ->
        for (i in 0 until view.childCount) {
            val chip = view.getChildAt(i)
            chip.isClickable = chip.id != id
        }
        val role = when (id) {
            R.id.chip_creditor -> DebtRole.CREDITOR
            R.id.chip_debtor -> DebtRole.DEBTOR
            else -> DebtRole.CREDITOR
        }
        onNext(role)
    }
    view.setOnCheckedChangeListener(listener)
}